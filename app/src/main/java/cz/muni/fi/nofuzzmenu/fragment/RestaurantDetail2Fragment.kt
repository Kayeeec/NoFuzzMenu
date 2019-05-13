package cz.muni.fi.nofuzzmenu.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurant

/**
 * A placeholder fragment containing a simple view.
 */
class RestaurantDetail2Fragment : Fragment() {

    private val restaurant: ZomatoRestaurant =

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_detail2, container, false)
    }
}
