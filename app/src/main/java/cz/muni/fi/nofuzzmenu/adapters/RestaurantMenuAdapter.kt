package cz.muni.fi.nofuzzmenu.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.activity.RestaurantDetailActivity
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto

class RestaurantMenuAdapter(private var menus: List<MenuItemDto>) : RecyclerView.Adapter<RestaurantMenuAdapter.ViewHolder>() {

    fun refresh(menus: List<MenuItemDto>) {
        this.menus = menus
        notifyDataSetChanged()
    }

    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_menu_item, parent, false)
        )
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menus[position])
    }

    /**
     * Adapter needs to know how many items are there to show.
     */
    override fun getItemCount(): Int {
        return menus.size
    }


    /**
     * Reusable ViewHolder objects.
     * TODO: put necessary data here, according to mockups
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.numbered_name)
        var cost: TextView = itemView.findViewById(R.id.cost)

        fun bind(menuItem: MenuItemDto) {
            // thumbnail todo
            name.text = "" + (adapterPosition + 1) + ". " + menuItem.name //todo format
            cost.text = menuItem.cost.toString() //todo format
        }
    }
}