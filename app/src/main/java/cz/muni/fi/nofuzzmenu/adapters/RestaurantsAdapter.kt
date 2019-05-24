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
        this.restaurants = restaurants.sortedBy { it.distance }.toMutableList()
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
        this.restaurants.sortBy { it.distance }
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
     * TODO: put necessary data here, according to mockups
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
        var name: TextView = itemView.findViewById(R.id.name)
        var distance: TextView = itemView.findViewById(R.id.distance)
        var cuisines = itemView.findViewById<TextView>(R.id.cuisine)

        fun bind(restaurant: RestaurantInfoDto) {
            //val context = avatar.context
            // TODO: use Glide to load restaurant thumbnails
            //Glide.with(context)
            //    .load(user.avatarUrl)
            //    .into(avatar)
            thumbnail.setImageResource(RestaurantThumbnails.getThumbnailDrawableAndIncrement())
            name.text = restaurant.name
            distance.text = restaurant.distanceString
            cuisines.text = restaurant.cuisines
        }
    }
}