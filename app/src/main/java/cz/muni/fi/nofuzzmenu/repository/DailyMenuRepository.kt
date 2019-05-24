package cz.muni.fi.nofuzzmenu.repository

import android.util.Log
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi
import cz.muni.fi.nofuzzmenu.zomato.models.DailyMenu
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoMenu
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyMenuRepository() : BaseRepository() {
    private val TAG = this::class.java.name
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1") //todo api key storage
    private val formats = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    )

    suspend fun getMenu(restaurantId: String): MutableList<MenuItemDto> {
        val restaurantMenu = RealmUtils.getRestaurantMenu(restaurantId)
        if (restaurantMenu.isNotEmpty()){  // todo: on errors the menu is always empty, should we call api? age?
            Log.d(TAG, "Fetching restaurant #${restaurantId} menu from database.")
            return restaurantMenu
        } else {
            val fromApi = fetchFromApi(restaurantId)
            RealmUtils.updateRestaurant(restaurantId, fromApi)
            return fromApi
        }
    }

    private suspend fun fetchFromApi(restaurantId: String): MutableList<MenuItemDto> {
        Log.d(TAG, "Fetching restaurant #${restaurantId} menu from api.")
        //        val call = zomatoApi.service.getMenuAsync(apiKey = zomatoApi.apiKey, restaurantId = "16506939") //working menu display
        val call = zomatoApi.service.getMenuAsync(apiKey = zomatoApi.apiKey, restaurantId = restaurantId)

        val response = safeApiCall(
            call = { call.await() },
            errorMessage = "Error fetching restaurant's daily menu from zomato."
        )
        val result = mutableListOf<MenuItemDto>()
        response?.daily_menus?.forEach { zomatoMenu: ZomatoMenu ->
            val dailyMenu = zomatoMenu.daily_menu ?: return@forEach
            if (isTodaysMenu(dailyMenu)) {
                dailyMenu.dishes?.forEach {
                    val dish = it.dish
                    if (dish.name.isNotEmpty()){
                        result.add(MenuItemDto(name = dish.name, cost = dish.price))
                    }
                }
            }
        }
        return result
    }

    private fun isTodaysMenu(dailyMenu: DailyMenu): Boolean {
        val startDate = dateFromString(dailyMenu.start_date)
        val endDate = dateFromString(dailyMenu.end_date)
        val today = LocalDate.now()

        if (startDate != null && endDate != null) {
            return today in startDate..endDate
        }
        if (startDate == null && endDate == null){
            return true // todo what to return? just include in the list? failed to parse? probably won't be null tho
        }
        if (startDate != null) return today.isEqual(startDate)
        return today.isEqual(endDate)
    }

    //assume api fetches local date (in zone of the user)
    private fun dateFromString(string: String?): LocalDate?{
        if (string.isNullOrBlank()) return null
        for (format in formats){
            try {
                val date = LocalDate.parse(string, format)
                if (date != null ) return date
            } catch (e: Exception){
                // do nothing
            }
        }
        return null
    }

}