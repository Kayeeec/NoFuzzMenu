package cz.muni.fi.nofuzzmenu.repository

import android.location.Location
import android.util.Log
import cz.muni.fi.nofuzzmenu.BuildConfig
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi

class RestaurantRepository : BaseRepository() {
    private val TAG = this.javaClass.name
    private val zomatoApi = ZomatoApi(BuildConfig.ZOMATO_API_KEY) //todo api key storage
    private val startLocation = Location("")

    //todo default count from settings
    suspend fun getRestaurants(
        parameters: Map<String, String>,
        start: Int = 0,
        count: Int = 20
    ): MutableList<RestaurantInfoDto> {
        resolveStartingLocation(parameters)

        val savedRestaurants = RealmUtils.getRestaurantsForRequestFromDatabase(
            startLocation.longitude,
            startLocation.latitude,
            parameters["radius"]!!.toDouble(),
            start,
            count
        )
        if (savedRestaurants.isNotEmpty()) {
            Log.d(TAG, "Getting restaurants from database.")
            return savedRestaurants
        } else {
            val fromApi = fetchFromApi(parameters, start, count)
            RealmUtils.saveRequest(
                startLocation.longitude,
                startLocation.latitude,
                parameters["radius"]!!.toDouble(),
                start,
                count,
                fromApi
            )
            return fromApi
        }
    }

    private suspend fun fetchFromApi(
        parameters: Map<String, String>,
        start: Int,
        count: Int
    ): MutableList<RestaurantInfoDto> {
        Log.d(TAG, "Fetching restaurants from API.")
        val call = zomatoApi.service.getRestaurantsAsync(
            apiKey = zomatoApi.apiKey,
            query = parameters["query"],
            latitude = parameters["latitude"],
            longitude = parameters["longitude"],
            radius = parameters["radius"]?.toDouble(),
            cuisines = parameters["cuisines"], // TODO use list, put here as comma-separated list
            sortBy = "real_distance",
            sortOrder = "asc",
            start = start,
            count = count
        )
        val zomatoResponse = safeApiCall(
            call = { call.await() },
            errorMessage = "Error fetching restaurants from zomato." // todo proper exception
        )

        val zomatoRestaurants = mutableListOf<RestaurantInfoDto>()
        zomatoResponse?.restaurants?.forEach {
            val r = it.restaurant
            val endLocation = getLocation(r.location.latitude, r.location.longitude)
            val distance = startLocation.distanceTo(endLocation)
            // filtering has to be done manually, Zomato search does not filter
            val radius = parameters["radius"]?.toDouble()
            if (radius == null){
                zomatoRestaurants.add(RestaurantInfoDto(r.id, r.name, r.location.address, r.cuisines, distance, r.price_range, r.user_rating.aggregate_rating))
            } else if (distance < radius) {
                zomatoRestaurants.add(RestaurantInfoDto(r.id, r.name, r.location.address, r.cuisines, distance, r.price_range, r.user_rating.aggregate_rating))
            }
        }
        return sortRestaurants(parameters, zomatoRestaurants)
    }

    private fun resolveStartingLocation(parameters: Map<String, String>) {
        parameters["latitude"]?.toDoubleOrNull()?.let {
            startLocation.latitude = it
        }
        parameters["longitude"]?.toDoubleOrNull()?.let {
            startLocation.longitude = it
        }
    }

    private fun getLocation(latitude: String, longitude: String): Location {
        val location = Location("")
        if (latitude.isNotEmpty()) {
            location.latitude = latitude.toDoubleOrNull() ?: 0.0
        }
        if (longitude.isNotEmpty()) {
            location.longitude = longitude.toDoubleOrNull() ?: 0.0
        }
        return location
    }

    private fun sortRestaurants(parameters: Map<String, String>, restaurants: MutableList<RestaurantInfoDto>): MutableList<RestaurantInfoDto> {
        val sortOrder = parameters["order"] ?: "asc"
        val sortCriteria = parameters["sortBy"] ?: "real_distance"
        val output = ArrayList(restaurants)
        if (sortOrder == "asc") {
            Log.d(TAG, "Sorting asc")
            output.sortBy(sortFunctions.getValue(sortCriteria))
        } else {
            Log.d(TAG, "Sorting desc")
            output.sortByDescending(sortFunctions.getValue(sortCriteria))
        }

        return output
    }

    companion object {
        val sortFunctions = mapOf(
            Pair("real_distance", {r:RestaurantInfoDto -> r.distance}),
            Pair("cost", {r:RestaurantInfoDto -> r.price_range}),
            Pair("rating", {r:RestaurantInfoDto -> r.rating})
        )
    }
}