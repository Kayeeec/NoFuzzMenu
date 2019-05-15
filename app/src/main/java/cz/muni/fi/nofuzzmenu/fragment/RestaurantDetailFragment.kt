package cz.muni.fi.nofuzzmenu.fragment

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.RestaurantDetailActivity
import cz.muni.fi.nofuzzmenu.adapters.RestaurantMenuAdapter
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import java.net.URLEncoder

/**
 * A placeholder fragment containing a simple view.
 * todo add up-down drag to reload menu?
 */
class RestaurantDetailFragment : Fragment() {

    private val adapter = RestaurantMenuAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(RestaurantDetailFragment::class.java.simpleName,  "onCreateView(...)")
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(RestaurantDetailFragment::class.java.simpleName,  "onViewCreated(...)")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val restaurantActivity = activity as RestaurantDetailActivity
        val restaurant = restaurantActivity.getRestaurant()

        // todo menu load
        val menus = listOf(
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

        val name = view?.findViewById<TextView>(R.id.name)
        name?.text = restaurant?.name

//        detail quisines , address
        val cuisines = view?.findViewById<TextView>(R.id.detail_cuisines)
        cuisines?.text = restaurant?.cuisines

        val address = view?.findViewById<TextView>(R.id.detail_address)
        address?.text = restaurant?.address

        val distance = view?.findViewById<TextView>(R.id.detail_distance)
        distance?.text = restaurant?.distance

//        nav, web, menuweb btn todo
        val webBtn = view?.findViewById<MaterialButton>(R.id.web_btn)
        val navBtn = view?.findViewById<MaterialButton>(R.id.navigate_btn)
        setGoogleSearchLinkBtn(restaurant, webBtn)
        setNavigationBtn(restaurant?.address, navBtn)


//        restaurant detail menu items
        val list = view?.findViewById<RecyclerView>(R.id.restaurant_detail_menu_items)
        list?.layoutManager = LinearLayoutManager(context)
        list?.adapter = adapter
        list?.setHasFixedSize(true)

        menus.let { adapter.refresh(it.toList()) }
    }

    private fun refreshMenuList(response: String?) {
        if (response == null) {
            return //todo: error toast + retry
        }
        // will consume response from used service and refresh the list
//        adapter.refresh(menuItems)
    }

    private fun loadMenu(searchParameters: Map<String, String?>) {
        // TODO: will use service to load menu and refresh the list

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


}
