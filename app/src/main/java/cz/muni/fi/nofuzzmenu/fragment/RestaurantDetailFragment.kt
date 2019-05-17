package cz.muni.fi.nofuzzmenu.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

/**
 * A placeholder fragment containing a simple view.
 * todo add up-down drag to reload menu?
 */
class RestaurantDetailFragment : Fragment() {
    private val TAG = this::class.java.name

    private val adapter = RestaurantMenuAdapter(ArrayList())

    private var liveMenus = MutableLiveData<MutableList<MenuItemDto>>()
    private val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val repository = DailyMenuRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,  "onCreateView(...)")
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,  "onViewCreated(...)")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val restaurantActivity = activity as RestaurantDetailActivity
        val restaurant = restaurantActivity.getRestaurant()

        val name = view?.findViewById<TextView>(R.id.name)
        name?.text = restaurant?.name

//        detail quisines , address
        val cuisines = view?.findViewById<TextView>(R.id.detail_cuisines)
        cuisines?.text = restaurant?.cuisines

        val address = view?.findViewById<TextView>(R.id.detail_address)
        address?.text = restaurant?.address

        val distance = view?.findViewById<TextView>(R.id.detail_distance)
        distance?.text = restaurant?.distanceString

//        nav, web, menuweb btn todo
        val webBtn = view?.findViewById<MaterialButton>(R.id.web_btn)
        val navBtn = view?.findViewById<MaterialButton>(R.id.navigate_btn)
        setGoogleSearchLinkBtn(restaurant, webBtn)
        setNavigationBtn(restaurant?.address, navBtn)

//        restaurant detail menu items
        try {
            loadMenu(restaurant)
        } finally {
            hideLoading()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAllRequests()
    }

    fun cancelAllRequests() = coroutineContext.cancel()

    private fun loadMenu(restaurant: RestaurantInfoDto?) {
        hideNoDataText()
        showLoading()
        if (restaurant == null) {
            Log.d(TAG, "no restaurant")
            hideLoading()
            showNoDataText()
            return
        }
        val list = view?.findViewById<RecyclerView>(R.id.restaurant_detail_menu_items)
        list?.layoutManager = LinearLayoutManager(context)
        list?.adapter = adapter
        list?.setHasFixedSize(true)

        scope.launch {
            val menus = repository.getMenu(restaurant.id)
            liveMenus.postValue(menus)
        }
        liveMenus.observe(this, Observer { menus ->
            if (menus.isNotEmpty()){
                adapter.refresh(menus)
                list?.visibility = View.VISIBLE
            } else {
                showNoDataText()
            }
        })
    }

    private fun showLoading() {
        val loading = view?.findViewById<ProgressBar>(R.id.menu_loading)
        loading?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        val loading = view?.findViewById<ProgressBar>(R.id.menu_loading)
        loading?.visibility = View.GONE
    }

    private fun showNoDataText() {
        val noDataText = view?.findViewById<TextView>(R.id.no_data_message)
        noDataText?.visibility = View.VISIBLE
    }

    private fun hideNoDataText() {
        val noDataText = view?.findViewById<TextView>(R.id.no_data_message)
        noDataText?.visibility = View.GONE
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
        MenuItemDto(name="Salát Caesar longass name that should probably brak and make averything weird lorem ipsum sit amet dolor color sum nomer uno dos tres quatro sinco sicno seis you are pretty fly", cost = "10005 Kč"),
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
