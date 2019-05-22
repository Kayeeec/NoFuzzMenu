package cz.muni.fi.nofuzzmenu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.nofuzzmenu.R
import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto

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

        var number: TextView = itemView.findViewById(R.id.detail_item_number)
        var name: TextView = itemView.findViewById(R.id.detail_item_name)
        var cost: TextView = itemView.findViewById(R.id.detail_item_cost)

        fun bind(menuItem: MenuItemDto) {
            // thumbnail todo
            number.text = formatItemNumber(adapterPosition)
            name.text = menuItem.name //todo format
            cost.text = menuItem.cost.toString() //todo format
        }

        private fun formatItemNumber(adapterPosition: Int): String {
            val index = adapterPosition + 1
            if (index in 0..9) return " ${index}." //adds space before single digit line numbers
            return "${index}."
        }
    }
}