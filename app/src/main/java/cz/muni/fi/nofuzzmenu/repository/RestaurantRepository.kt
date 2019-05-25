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

    suspend fun getRestaurants(
        parameters: Map<String, String>,
        start: Int = 0,
        count: Int = 20
    ): MutableList<RestaurantInfoDto> {
        resolveStartingLocation(parameters)
        if (startLocation.latitude == Double.NaN || startLocation.longitude == Double.NaN) {
            return mutableListOf()
        }

        val savedRestaurants = RealmUtils.getRestaurantsForRequestFromDatabase(
            startLocation.longitude,
            startLocation.latitude,
            parameters["radius"]?.toDouble(),
            start,
            count
        )
        if (savedRestaurants.isNotEmpty()) {
            Log.d(TAG, "Getting restaurants from database.")
            // filter and order
            return sortAndFilterRestaurants(parameters, savedRestaurants)
        } else {
            val fromApi = fetchFromApi(parameters, start, count)
            RealmUtils.saveRequest(
                startLocation.longitude,
                startLocation.latitude,
                parameters["radius"]?.toDouble(),
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
            cuisines = parameters["cuisines"],
            sortBy = "real_distance",
            sortOrder = "asc",
            start = start,
            count = count
        )
        val zomatoResponse = safeApiCall(
            call = { call.await() },
            errorMessage = "Error fetching restaurants from zomato."
        )

        val zomatoRestaurants = mutableListOf<RestaurantInfoDto>()
        zomatoResponse?.restaurants?.forEach {
            val r = it.restaurant
            val endLocation = getLocation(r.location.latitude, r.location.longitude)
            val distance = startLocation.distanceTo(endLocation)
            // filtering has to be done manually, Zomato search does not filter
            zomatoRestaurants.add(RestaurantInfoDto(r.id, r.name, r.location.address, r.cuisines, distance, r.price_range, r.user_rating.aggregate_rating))

        }
        return sortAndFilterRestaurants(parameters, zomatoRestaurants)
    }

    private fun resolveStartingLocation(parameters: Map<String, String>) {
        startLocation.latitude = parameters["latitude"]?.toDoubleOrNull() ?: Double.NaN
        startLocation.longitude = parameters["longitude"]?.toDoubleOrNull() ?: Double.NaN
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

    private fun sortAndFilterRestaurants(parameters: Map<String, String>, restaurants: MutableList<RestaurantInfoDto>): MutableList<RestaurantInfoDto> {
        val radius = parameters["radius"]?.toDouble()
        val output = mutableListOf<RestaurantInfoDto>()

        if (radius != null) {
            restaurants.forEach { r ->
                if (r.distance < radius) {
                    output.add(r)
                }
            }
        }

        val sortOrder = parameters["order"] ?: "asc"
        val sortCriteria = parameters["sortBy"] ?: "real_distance"
        if (sortOrder == "asc") {
            Log.d(TAG, "Sorting asc")
            when (sortCriteria){
                "real_distance" -> output.sortBy { it.distance }
                "cost" -> output.sortBy { it.price_range }
                "rating" -> output.sortBy { it.rating }
            }
        } else {
            Log.d(TAG, "Sorting desc")
            when (sortCriteria){
                "real_distance" -> output.sortByDescending { it.distance }
                "cost" -> output.sortByDescending { it.price_range }
                "rating" -> output.sortByDescending { it.rating }
            }
        }

        Log.d(TAG, "Sorted output: ${output.forEach {println(it)}}")
        return output
    }
}