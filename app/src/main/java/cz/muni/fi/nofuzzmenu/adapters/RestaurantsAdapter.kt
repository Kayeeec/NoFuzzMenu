package cz.muni.fi.nofuzzmenu.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.RestaurantDetailActivity
import cz.muni.fi.nofuzzmenu.activity.SearchSettingsActivity
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurant
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoRestaurantWrapper

class RestaurantsAdapter(private var restaurants: List<ZomatoRestaurant>) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    fun refresh(restaurants: List<ZomatoRestaurantWrapper>) {
        this.restaurants = restaurants.map { restaurantWrapper -> restaurantWrapper.restaurant }
        notifyDataSetChanged()
    }

    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_restaurant, parent, false)
        )
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurants[position])
        holder.itemView.setOnClickListener {
            Log.d("restaurantAdapter", "restaurant click listener")
            val intent: Intent = Intent(it.context, RestaurantDetailActivity::class.java)
            intent.putExtra("Restaurant", restaurants[position])
            it.context.startActivity(intent)
        }
    }

    /**
     * Adapter needs to know how many items are there to show.
     */
    override fun getItemCount(): Int {
        return restaurants.size
    }


    /**
     * Reusable ViewHolder objects.
     * TODO: put necessary data here, according to mockups
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.name)
        var address: TextView = itemView.findViewById(R.id.address)

        fun bind(restaurant: ZomatoRestaurant) {
            //val context = avatar.context
            // TODO: use Glide to load restaurant thumbnails
            //Glide.with(context)
            //    .load(user.avatarUrl)
            //    .into(avatar)
            name.text = restaurant.name
            address.text = restaurant.location.address
        }
    }
}