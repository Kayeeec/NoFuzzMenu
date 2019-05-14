package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto

class RestaurantDetailActivity : AppCompatActivity() {

    private var restaurant: RestaurantInfoDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(RestaurantDetailActivity::class.java.simpleName,  "onCreate(...)")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        restaurant = intent.getSerializableExtra("Restaurant") as RestaurantInfoDto
        title = restaurant?.name

    }

    fun getRestaurant(): RestaurantInfoDto? {
        return restaurant
    }
}
