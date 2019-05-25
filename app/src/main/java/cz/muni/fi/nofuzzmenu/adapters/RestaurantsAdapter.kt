package cz.muni.fi.nofuzzmenu.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.RestaurantDetailActivity
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.utils.RestaurantThumbnails


class RestaurantsAdapter(private var restaurants: MutableList<RestaurantInfoDto>) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    fun refresh(restaurants: List<RestaurantInfoDto>) {
        this.restaurants.addAll(restaurants.toMutableList())
        RestaurantThumbnails.resetCounter()
        notifyDataSetChanged()
    }

    fun clear() {
        this.restaurants.clear()
        RestaurantThumbnails.resetCounter()
        notifyDataSetChanged()
    }

    fun addAll(restaurants: List<RestaurantInfoDto>){
        this.restaurants.addAll(restaurants)
        this.restaurants
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
            val intent = Intent(it.context, RestaurantDetailActivity::class.java)
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
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
        var name: TextView = itemView.findViewById(R.id.name)
        var rating: TextView = itemView.findViewById(R.id.rating)
        var priceRange: TextView = itemView.findViewById(R.id.price_range)
        var distance: TextView = itemView.findViewById(R.id.distance)
        var cuisines = itemView.findViewById<TextView>(R.id.cuisine)

        fun bind(restaurant: RestaurantInfoDto) {
            thumbnail.setImageResource(RestaurantThumbnails.getThumbnailDrawableAndIncrement())
            name.text = restaurant.name
            rating.text = restaurant.rating()
            priceRange.text = restaurant.priceRange()
            distance.text = restaurant.distanceString
            cuisines.text = restaurant.cuisines
        }
    }
}