package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var restaurant: RestaurantInfoDto

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(RestaurantDetailActivity::class.java.simpleName,  "onCreate(...)")

        restaurant = intent.getSerializableExtra("Restaurant") as RestaurantInfoDto
        title = restaurant.name

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)


    }

    fun getRestaurant(): RestaurantInfoDto {
        return restaurant
    }
}
