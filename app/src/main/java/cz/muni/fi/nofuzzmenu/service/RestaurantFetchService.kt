package cz.muni.fi.nofuzzmenu.service

import cz.muni.fi.nofuzzmenu.adapters.RestaurantsAdapter
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurantsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: when we have more APIs, we'll need to sync the info
// -> investigate synchronization + view updateing
// consider https://medium.com/mindorks/android-recyclerview-pagination-with-paging-library-using-mvvm-rxjava-dagger-2-and-livedata-b0489ecbbfc0
class RestaurantFetchService(private val start: Int, private val count: Int, val adapter: RestaurantsAdapter) {
    // TODO: move api key to some constants
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1")
    private val zomatoRestaurants = ArrayList<RestaurantInfoDto>()

    fun fetchRestaurants(parameters: Map<String, String>): List<RestaurantInfoDto> {
        fetchZomatoRestaurants(parameters)
        return zomatoRestaurants
    }

    private fun fetchZomatoRestaurants(searchParameters: Map<String, String>) { //: List<ZomatoRestaurantWrapper>
        val call = zomatoApi.service.getRestaurants(
            apiKey = zomatoApi.apiKey,
            query = searchParameters["query"],
            latitude = searchParameters["latitude"],
            longitude = searchParameters["longitude"],
            radius = searchParameters["radius"]?.toDouble(),
            cuisines = searchParameters["cuisines"], // TODO use list, put here as comma-separated list
            sortBy = searchParameters["sortBy"],
            sortOrder = searchParameters["sortOrder"],
            start = start,
            count = count
        )
        call.enqueue(object : Callback<ZomatoRestaurantsListResponse> {

            override fun onResponse(call: Call<ZomatoRestaurantsListResponse>, response: Response<ZomatoRestaurantsListResponse>) {
                val body = response.body()
                addZomatoRestaurants(body)
            }

            override fun onFailure(call: Call<ZomatoRestaurantsListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun addZomatoRestaurants(response: ZomatoRestaurantsListResponse?) {
        if (response == null) {
            return
        }

        for (restaurant in response.restaurants) {
            val r = restaurant.restaurant
            zomatoRestaurants.add(RestaurantInfoDto(r.name, r.location.address, r.url, r.cuisines, r.menu_url))
            adapter.refresh(zomatoRestaurants)
        }
    }
}