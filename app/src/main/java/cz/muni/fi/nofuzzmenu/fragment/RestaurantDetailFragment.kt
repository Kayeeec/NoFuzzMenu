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
import java.net.URLEncoder

/**
 * A placeholder fragment containing a simple view.
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
        val menu = restaurantActivity.getMenu()

        val name = view?.findViewById<TextView>(R.id.name)
        name?.text = restaurant?.name

//        detail quisines , address
        val cuisines = view?.findViewById<TextView>(R.id.detail_cuisines)
        cuisines?.text = restaurant?.cuisines

        val address = view?.findViewById<TextView>(R.id.detail_address)
        address?.text = restaurant?.address

//        nav, web, menuweb btn todo
        val webBtn = view?.findViewById<MaterialButton>(R.id.web_btn)
        val navBtn = view?.findViewById<MaterialButton>(R.id.navigate_btn)
        val menuWebBtn = view?.findViewById<MaterialButton>(R.id.menu_web_btn)
        setWebsiteLinkBtn(restaurant?.web, webBtn)
        setWebsiteLinkBtn(restaurant?.menu_url, menuWebBtn)
        setNavigationBtn(restaurant?.address, navBtn)


//        restaurant detail menu items
        val list = view?.findViewById<RecyclerView>(R.id.restaurant_detail_menu_items)
        list?.layoutManager = LinearLayoutManager(context)
        list?.adapter = adapter
        list?.setHasFixedSize(true)

        menu?.menus?.let { adapter.refresh(it.toList()) }
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

    private fun setWebsiteLinkBtn(url: String?, button: MaterialButton?){
        if (!url.isNullOrBlank() && button != null){
            button.isEnabled = true
            button.setOnClickListener {
                val parseUri = parseUri(url)
                Log.d("RestauranteDetailFragment.setWebsiteLink.parseUri: ", parseUri.toString())
                startActivity(Intent(Intent.ACTION_VIEW, parseUri))
            }
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
