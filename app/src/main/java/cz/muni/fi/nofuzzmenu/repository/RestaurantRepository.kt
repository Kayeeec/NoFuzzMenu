package cz.muni.fi.nofuzzmenu.repository

import android.location.Location
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi

class RestaurantRepository() : BaseRepository() {
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1") //todo api key storage
    private val startLocation = Location("")

    //todo default count from settings
    suspend fun getRestaurants(
        parameters: Map<String, String>,
        start: Int = 0,
        count: Int = 20
    ): MutableList<RestaurantInfoDto> {
        resolveStartingLocation(parameters)
        val call = zomatoApi.service.getRestaurantsAsync(
            apiKey = zomatoApi.apiKey,
            query = parameters["query"],
            latitude = parameters["latitude"],
            longitude = parameters["longitude"],
            radius = parameters["radius"]?.toDouble(),
            cuisines = parameters["cuisines"], // TODO use list, put here as comma-separated list
            sortBy = parameters["sortBy"],
            sortOrder = parameters["sortOrder"],
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
            zomatoRestaurants.add(RestaurantInfoDto(r.id, r.name, r.location.address, r.cuisines, distance))
        }
        return zomatoRestaurants
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

}