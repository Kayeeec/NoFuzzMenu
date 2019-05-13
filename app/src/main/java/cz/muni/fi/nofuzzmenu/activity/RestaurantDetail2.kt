package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurant

import kotlinx.android.synthetic.main.activity_restaurant_detail2.*

class RestaurantDetail2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail2)
        setSupportActionBar(toolbar)
    }

    fun getRestaurant(): ZomatoRestaurant {
        return intent.getSerializableExtra("Restaurant") as ZomatoRestaurant
    }

}
