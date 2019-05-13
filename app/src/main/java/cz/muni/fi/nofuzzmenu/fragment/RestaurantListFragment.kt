package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.adapters.RestaurantsAdapter
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.service.RestaurantFetchService
import kotlin.collections.HashMap

class RestaurantListFragment : Fragment() {

    // TODO: pagination https://stackoverflow.com/questions/16661662/how-to-implement-pagination-in-android-listview
    private val adapter = RestaurantsAdapter(ArrayList())
    private var restaurants = ArrayList<RestaurantInfoDto>()
    private lateinit var restaurantFetchingService: RestaurantFetchService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        val searchParameters = loadSavedParameters()
        restaurantFetchingService = RestaurantFetchService(0, 20, adapter)
        restaurants.addAll(restaurantFetchingService.fetchRestaurants(searchParameters))

        val list = view.findViewById<RecyclerView>(android.R.id.list)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        list.setHasFixedSize(true)

        return view
    }

    private fun loadSavedParameters(): Map<String, String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val all = prefs.all
        val result = HashMap<String, String>()
        for (item in all) {
            if (item.value !is String){
                result[item.key] = item.value.toString()
            }
        }
        return result
    }

    private fun addRestaurants(response: ZomatoRestaurantsListResponse?) {
        if (response == null) {
            return
        }

        for (restaurant in response.restaurants) {
            val r = restaurant.restaurant
            restaurants.add(RestaurantInfoDto(r.name, r.location.address, r.cuisines))
        }

        adapter.refresh(restaurants)
    }
}