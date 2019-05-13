package cz.muni.fi.nofuzzmenu.activity

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto

class RestaurantDetailActivity() : AppCompatActivity(), Parcelable {

    private var restaurant: RestaurantInfoDto? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_restaurant_detail)
        Log.d("rd_activity", "before fragment")

        restaurant = intent.getSerializableExtra("Restaurant") as RestaurantInfoDto

        Log.d("loaded restaurant", printRestaurant(restaurant!!))

        /*
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SearchSettingsFragment())
            .commit()


*/
        Log.d("rd_activity", "after fragment")
    }

    private fun printRestaurant(restaurant: RestaurantInfoDto): String {
        return "${restaurant.name}, ${restaurant.menu_url}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantDetailActivity> {
        override fun createFromParcel(parcel: Parcel): RestaurantDetailActivity {
            return RestaurantDetailActivity(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantDetailActivity?> {
            return arrayOfNulls(size)
        }
    }
}