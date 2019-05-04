package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.adapters.RestaurantsAdapter
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurant
import kotlinx.android.synthetic.main.restaurant_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class RestaurantListFragment : Fragment() {

    // TODO: move api key to some constants
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1")
   private lateinit var adapter: RestaurantsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.restaurant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchParameters = loadSavedParameters()
        loadRestaurants(searchParameters)

        list.adapter = RestaurantsAdapter(listOf())
        list.layoutManager = LinearLayoutManager(context)
        list.setHasFixedSize(true)
    }

    /**
     * Loads a list of nearby restaurants based on current settings.
     */
    private fun loadRestaurants(searchParameters: Map<String, String?>) {
        // TODO: when we have more APIs, we'll need to sync the info
        val call = zomatoApi.service.getRestaurants(
            apiKey = zomatoApi.apiKey,
            query = searchParameters["query"],
            latitude = searchParameters["latitude"],
            longitude = searchParameters["longitude"],
            radius = searchParameters["radius"]?.toDouble(),
            count = searchParameters["count"]?.toInt(),
            cuisines = searchParameters["cuisines"], // TODO use list, put here as comma-separated list
            sortBy = searchParameters["sortBy"],
            sortOrder = searchParameters["sortOrder"]
            )
        call.enqueue(object : Callback<List<ZomatoRestaurant>> {

            override fun onResponse(call: Call<List<ZomatoRestaurant>>, response: Response<List<ZomatoRestaurant>>) {
                val body = response.body()
                populateList(body)
            }

            override fun onFailure(call: Call<List<ZomatoRestaurant>>, t: Throwable) {
                t.printStackTrace()
            }
        })
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

    private fun populateList(restaurants: List<ZomatoRestaurant>?) {
        if (restaurants == null) {
            return
        }

        adapter.refresh(restaurants)
    }
}