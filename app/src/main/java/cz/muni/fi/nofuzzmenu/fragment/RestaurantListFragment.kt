package cz.muni.fi.nofuzzmenu.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.adapters.PaginationScrollListener
import cz.muni.fi.nofuzzmenu.adapters.RestaurantsAdapter
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.repository.RestaurantRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class RestaurantListFragment : Fragment() {
    private val TAG = this.javaClass.name
    private val MAX_RESTAURANTS = 140

    // TODO: pagination https://stackoverflow.com/questions/16661662/how-to-implement-pagination-in-android-listview
    private val adapter = RestaurantsAdapter(ArrayList())
    private var liveRestaurants = MutableLiveData<MutableList<RestaurantInfoDto>>()
    private val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val repository = RestaurantRepository()
    private var start = 0
    private val count = 20

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(android.R.id.list)
        val layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager
        list.adapter = adapter
        list.setHasFixedSize(true)
        list.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        list.visibility = View.GONE

        val searchParameters = loadSavedParameters()
        Log.d(TAG, "Loaded search parameters: $searchParameters")

        liveRestaurants.observe(this, Observer {restaurants ->
            if (restaurants.isNotEmpty()){
                adapter.refresh(restaurants)
                list.visibility = View.VISIBLE
                hideNoDataText()
            } else if (adapter.itemCount == 0) {
                showNoDataText()
            }
            hideLoading()
        })

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            loadRestaurants(searchParameters, start, count)
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.swipeRefresh1, R.color.swipeRefresh2, R.color.swipeRefresh3, R.color.swipeRefresh4)
        loadRestaurants(searchParameters, start, count)

        /**
         * scroll listener for loading more items when the user scrolls to the bottom
         */
        list.addOnScrollListener(object: PaginationScrollListener(list.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                showLoading()
                start += count
                loadRestaurants(searchParameters, start, count)
            }

            override fun isLastPage(): Boolean {
                return start + count >= MAX_RESTAURANTS
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAllRequests()
    }

    private fun loadRestaurants(searchParameters: Map<String, String>, start: Int, count: Int) {
        showLoading()
        scope.launch {
            val restaurants = repository.getRestaurants(searchParameters, start, count)
            liveRestaurants.postValue(restaurants)
        }
    }

    private fun showNoDataText() {
        view?.findViewById<TextView>(R.id.no_data_message)?.visibility = View.VISIBLE
    }

    private fun hideNoDataText() {
        view?.findViewById<TextView>(R.id.no_data_message)?.visibility = View.GONE
    }

    private fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun loadSavedParameters(): Map<String, String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val all = prefs.all
        val result = HashMap<String, String>()
        for (item in all) {
            result[item.key] = item.value.toString()
        }
        return result
    }

    private fun cancelAllRequests() = coroutineContext.cancel()
}