package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.models.Restaurant
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurant
import io.realm.Realm
import kotlinx.android.synthetic.main.restaurant_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantListFragment : Fragment() {

    // TODO: move api key to some constants
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1")
    private val realm = Realm.getDefaultInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.restaurant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchParameters = loadSavedParameters()
        loadRestaurants(config)

        val restaurants = realm.where(Restaurant::class.java).findAll()
        list.adapter = RestaurantsAdapter(restaurants)
        list.layoutManager = LinearLayoutManager(context)
        list.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
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
                saveResult(response.body())
            }

            override fun onFailure(call: Call<List<ZomatoRestaurant>>, t: Throwable) {
                t.printStackTrace()
            }

            /**
             * IMPORTANT NOTE: It's better to write and read asynchronously. But for the sake of simplicity...
             */
            private fun saveResult(watchers: List<ZomatoRestaurant>?) {
                var realm: Realm? = null
                try {
                    realm = Realm.getDefaultInstance()
                    realm.executeTransaction { it.insertOrUpdate(watchers!!) }
                } finally {
                    realm?.close()
                }
            }
        })
    }
}