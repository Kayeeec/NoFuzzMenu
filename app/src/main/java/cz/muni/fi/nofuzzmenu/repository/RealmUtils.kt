package cz.muni.fi.nofuzzmenu.repository

import android.util.Log
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.models.MenuItem
import cz.muni.fi.nofuzzmenu.models.Request
import cz.muni.fi.nofuzzmenu.models.Restaurant
import cz.muni.fi.nofuzzmenu.utils.Mapper
import io.realm.Realm
import java.time.LocalDate

object RealmUtils{
    private val TAG = this.javaClass.name

    fun saveRequest(
        longitude: Double,
        latitude: Double,
        radius: Double,
        start: Int,
        count: Int,
        restaurants: MutableList<RestaurantInfoDto>
    ) {
        if (restaurants.isEmpty()) return // probably error, caching only valid results with restaurants
        Log.d(TAG, "Saving request - longitude: $longitude, latitude: $latitude, radius: $radius, start: $start, count: $count, restaurants: $restaurants")
        val today = LocalDate.now().toString()
        val realm = Realm.getDefaultInstance()

        realm.use {
            // pagination - try to find acceptable request to be extended
            var savedRequest = it.where(Request::class.java)
                .equalTo("latitude", latitude)
                .equalTo("longitude", longitude)
                .equalTo("date", today)
                .lessThanOrEqualTo("radius", radius)
                .lessThanOrEqualTo("lastStart", start)
                .lessThanOrEqualTo("lastCount", count).findFirst()

            val toBeSaved = restaurants.map { dto -> Mapper.dtoToRestaurant(dto) }

            it.beginTransaction()
            // if acceptable request not found, create new one in place
            if (savedRequest == null){
                savedRequest = it.createObject(Request::class.java)
                savedRequest?.latitude = latitude
                savedRequest?.longitude = longitude
                savedRequest?.date = today
            }
            // update what is to be updated with this new request
            savedRequest?.radius = radius
            savedRequest?.lastStart = start
            savedRequest?.lastCount = count

            // persist fetched restaurants and add them to the request
            toBeSaved.forEach { restaurant ->
                savedRequest?.restaurants?.add(it.copyToRealmOrUpdate(restaurant))
            }

            it.commitTransaction()
        }
    }

    /**
     * At startup on a background thread.
     */
    fun removeOldRequestsAsync(){
        Log.d(TAG, "Removing old requests.")
        val today = LocalDate.now().toString()
        val realm = Realm.getDefaultInstance()
        Log.d(TAG, "Path: " + realm.path)
        realm.use {
            it.executeTransactionAsync { bgRealm ->
                try {
                    val oldRequests = bgRealm.where(Request::class.java)
                        .notEqualTo("date", today)
                        .findAll()
                    Log.d(TAG, "Number of requests to remove = ${oldRequests.size}.")
                    val restaurants = oldRequests.map { it.restaurants }.flatten()
                    val menus = restaurants.map { it.menu }.flatten()

                    menus.forEach { menuItem: MenuItem -> menuItem.deleteFromRealm() }
                    restaurants.forEach { restaurant -> restaurant.deleteFromRealm() }
                    oldRequests.deleteAllFromRealm()
                    Log.d(TAG, "Old requests successfully deleted.")
                } catch (e: Exception){
                    Log.d(TAG, "Failed to delete old requests. exception: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    fun getRestaurantMenu(restaurantId: String): MutableList<MenuItemDto> {
        val realm = Realm.getDefaultInstance()
        realm.use {
             val r = it.where(Restaurant::class.java)
                .equalTo("restaurantId", restaurantId)
                .findFirst()
            r?.let { return Mapper.menusToDtos(r.menu) }
        }
        return mutableListOf()
    }

    fun updateRestaurant(restaurantId: String, menus: MutableList<MenuItemDto>) {
        Log.d(TAG, "Updating restaurant $restaurantId -- removing old and adding ${menus.size} new menus.")
        if (menus.isEmpty()) return
        val realm = Realm.getDefaultInstance()
        realm.use {
            val restaurant = it.where(Restaurant::class.java)
                .equalTo("restaurantId", restaurantId)
                .findFirst()
            if (restaurant != null){
                it.beginTransaction()
                restaurant.menu.deleteAllFromRealm()
                restaurant.menu.clear()
                menus.forEach { dto ->
                    val menuItem = it.copyToRealmOrUpdate(Mapper.dtoToMenuItem(dto))
                    restaurant.menu.add(menuItem)
                }
                it.commitTransaction()
            }
        }
    }

    fun getRestaurantsForRequestFromDatabase(longitude: Double, latitude: Double, radius: Double, start: Int, count: Int): MutableList<RestaurantInfoDto> {
        Log.d(TAG, "getRequestFromDatabase with params - longitude: $longitude, latitude: $latitude, radius: $radius, start: $start, count: $count")
        val realm = Realm.getDefaultInstance()
        val today = LocalDate.now().toString()
        realm.use {
            val request = it.where(Request::class.java)
                .equalTo("longitude", longitude)
                .equalTo("latitude", latitude)
                .greaterThanOrEqualTo("radius", radius)
                .equalTo("date", today)
                .greaterThanOrEqualTo("lastStart", start)
                .greaterThanOrEqualTo("lastCount", count)
                .findFirst()
            request?.let { return Mapper.restaurantsToDtoList(it.restaurants) }
        }
        return mutableListOf()
    }


}