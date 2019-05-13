package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantMenuDto
import cz.muni.fi.nofuzzmenu.fragment.RestaurantDetailFragment
import kotlinx.android.synthetic.main.item_restaurant.*

class RestaurantDetailActivity : AppCompatActivity() {

    private var restaurant: RestaurantInfoDto? = null
    private var menu: RestaurantMenuDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(RestaurantDetailActivity::class.java.simpleName,  "onCreate(...)")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        restaurant = intent.getSerializableExtra("Restaurant") as RestaurantInfoDto
        title = restaurant?.name

        //todo: fetch menu info
        menu = RestaurantMenuDto(
            menus = setOf(
                MenuItemDto(name="Guláš segedýnský", cost = 112.50),
                MenuItemDto(name="Rajská se sekanou", cost = 50.50),
                MenuItemDto(name="Ravioli", cost = 95.50),
                MenuItemDto(name="Salát Caesar", cost = 33.50)
            )
        )
    }

    fun getRestaurant(): RestaurantInfoDto? {
        return restaurant
    }

    fun getMenu(): RestaurantMenuDto? {
        return menu
    }

}
