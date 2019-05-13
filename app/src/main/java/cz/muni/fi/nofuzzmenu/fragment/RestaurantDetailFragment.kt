package cz.muni.fi.nofuzzmenu.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.RestaurantDetailActivity

/**
 * A placeholder fragment containing a simple view.
 */
class RestaurantDetailFragment : Fragment() {

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

        val restaurant = (activity as RestaurantDetailActivity).getRestaurant()
        val name = view?.findViewById<TextView>(R.id.detail_name)
        name?.text = restaurant?.name
    }
}
