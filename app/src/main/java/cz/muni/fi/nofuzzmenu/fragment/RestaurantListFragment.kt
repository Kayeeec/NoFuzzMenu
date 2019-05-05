package cz.muni.fi.nofuzzmenu.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.SearchSettingsActivity
import cz.muni.fi.nofuzzmenu.adapters.RestaurantsAdapter
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurantsListResponse
import kotlinx.android.synthetic.main.fragment_restaurant_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class RestaurantListFragment : Fragment() {

    // TODO: move api key to some constants
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1")
    private val adapter = RestaurantsAdapter(ArrayList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        val searchParameters = loadSavedParameters()
        loadRestaurants(searchParameters)

        val list = view.findViewById<RecyclerView>(android.R.id.list)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        list.setHasFixedSize(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn.setOnClickListener{_ ->
            Log.i("restaurant list", "switching activity via button")
            startActivity(Intent(context, SearchSettingsActivity::class.java))
        }
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
        call.enqueue(object : Callback<ZomatoRestaurantsListResponse> {

            override fun onResponse(call: Call<ZomatoRestaurantsListResponse>, response: Response<ZomatoRestaurantsListResponse>) {
                val body = response.body()
                populateList(body)
            }

            override fun onFailure(call: Call<ZomatoRestaurantsListResponse>, t: Throwable) {
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

    private fun populateList(response: ZomatoRestaurantsListResponse?) {
        if (response == null) {
            return
        }

        adapter.refresh(response.restaurants)
    }
}