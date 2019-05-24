package cz.muni.fi.nofuzzmenu.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.RestaurantDetailActivity
import cz.muni.fi.nofuzzmenu.adapters.RestaurantMenuAdapter
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.repository.DailyMenuRepository
import kotlinx.coroutines.*
import java.net.URLEncoder
import kotlin.coroutines.CoroutineContext


class RestaurantDetailFragment : Fragment() {
    private val TAG = this::class.java.name

    private val adapter = RestaurantMenuAdapter(ArrayList())

    private var liveMenus = MutableLiveData<MutableList<MenuItemDto>>()
    private val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val repository = DailyMenuRepository()

    private lateinit var restaurant: RestaurantInfoDto
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,  "onCreateView(...)")
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,  "onViewCreated(...)")
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener {
            loadMenu(restaurant)
        }
        swipeRefreshLayout?.setColorSchemeResources(R.color.swipeRefresh1, R.color.swipeRefresh2, R.color.swipeRefresh3, R.color.swipeRefresh4)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getRestaurantFromActivity()
        populateDetails()

        loadMenu(restaurant)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAllRequests()
    }

    private fun populateDetails() {
        val name = view?.findViewById<TextView>(R.id.name)
        name?.text = restaurant.name

        val cuisines = view?.findViewById<TextView>(R.id.detail_cuisines)
        cuisines?.text = restaurant.cuisines

        val address = view?.findViewById<TextView>(R.id.detail_address)
        address?.text = restaurant.address

        val distance = view?.findViewById<TextView>(R.id.detail_distance)
        distance?.text = restaurant.distanceString

        val webBtn = view?.findViewById<MaterialButton>(R.id.web_btn)
        val navBtn = view?.findViewById<MaterialButton>(R.id.navigate_btn)
        setGoogleSearchLinkBtn(restaurant, webBtn)
        setNavigationBtn(restaurant.address, navBtn)
    }

    private fun getRestaurantFromActivity() {
        val restaurantActivity = activity as RestaurantDetailActivity
        restaurant = restaurantActivity.getRestaurant()
    }

    fun cancelAllRequests(){
        Log.d(TAG, "Canceling all requests.")
        coroutineContext.cancel()
    }

    private fun loadMenu(restaurant: RestaurantInfoDto?) {
        showLoading()
        hideNoDataText()

        if (restaurant == null) {
            showNoDataText()
            return
        }
        val list = view?.findViewById<RecyclerView>(R.id.restaurant_detail_menu_items)
        list?.visibility = View.GONE
        list?.layoutManager = LinearLayoutManager(context)
        list?.adapter = adapter
        list?.setHasFixedSize(true)

        scope.launch {
            val menus = repository.getMenu(restaurant.id)
            liveMenus.postValue(menus)
        }
        liveMenus.observe(this, Observer { menus -> //todo better error handling? try catch etc
            if (menus.isNotEmpty()){
                adapter.refresh(menus)
                list?.visibility = View.VISIBLE
            } else {
                showNoDataText()
            }
            hideLoading()
        })
    }

    private fun showLoading() {
        swipeRefreshLayout?.isRefreshing = true
    }

    private fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun showNoDataText() {
        view?.findViewById<TextView>(R.id.no_data_message)?.visibility = View.VISIBLE
    }

    private fun hideNoDataText() {
        view?.findViewById<TextView>(R.id.no_data_message)?.visibility = View.GONE
    }

    private fun parseUri(url: String): Uri {
        val h = "http://"
        val hs = "https://"
        if(h !in url && hs !in url){
            return Uri.parse(h + url)
        }
        return Uri.parse(url)
    }

    private fun setGoogleSearchLinkBtn(restaurant: RestaurantInfoDto?, button: MaterialButton?){
        if (restaurant == null || button == null) return
        val query = URLEncoder.encode("${restaurant.name} ${restaurant.address}", "utf-8")
        val searchQuery = "http://www.google.com/#q=${query}"
        button.isEnabled = true
        val parseUri = parseUri(searchQuery)
        button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, parseUri))
        }
    }

    private fun setNavigationBtn(address: String?, button: MaterialButton?){
        if (button == null || address.isNullOrEmpty()) return
        val encAddress = URLEncoder.encode(address, "utf-8")
        val googleDirectionsUrl = "https://www.google.com/maps/dir/?api=1&destination=$encAddress"
        button.isEnabled = true
        button.setOnClickListener {
            val parseUri = parseUri(googleDirectionsUrl)
            Log.d("RestauranteDetailFragment.setWebsiteLink.parseUri: ", parseUri.toString())
            startActivity(Intent(Intent.ACTION_VIEW, parseUri))
        }

    }

    //todo delete on done
    private val mockMenu = listOf(
        MenuItemDto(name="Guláš segedýnský", cost = "90.50 Kč"),
        MenuItemDto(name="Rajská se sekanou", cost = "80 Kč"),
        MenuItemDto(name="Ravioli", cost = "150 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar longass name that should probably brake and make everything weird lorem ipsum sit amet dolor color sum nomer uno dos tres quatro sinco sicno seis you are pretty fly", cost = "10005 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč"),
        MenuItemDto(name="Salát Caesar", cost = "15 Kč")
    )


}
