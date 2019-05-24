package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

data class RestaurantInfoDto(
    var id: String,
    var name: String, //ok
    var address: String, // ok
    val cuisines: String?,
    val distance: Float,  // distance in meters from the location used in the request, for sorting
    val price_range: Float, // price range categories
    val rating: Float // aggregated user rating
): Serializable {
    var distanceString: String = "" // for display
    init {
        distanceString = convertDistanceToString(distance) //so it is computed only once on creation
    }

    fun priceRange(): String {
        return when (price_range){
            1.0f -> "      $"
            2.0f -> "     $$"
            3.0f -> "    $$$"
            4.0f -> "   $$$$"
            5.0f -> "  $$$$$"
            else -> "Unknown"
        }
    }

    fun rating(): String {
        return when (rating) {
            0.0f -> ""
            else -> "$rating/5"
        }
    }

    private fun convertDistanceToString(distance: Float): String {
        if (distance > 1000){
            val n = distance / 1000
            val fmtLocale = Locale.getDefault(Locale.Category.FORMAT) // todo: will need to get system locale?
            val formatter = NumberFormat.getInstance(fmtLocale)
            formatter.maximumFractionDigits = 1
            return "${formatter.format(n)}km"
        }
        return "${distance.roundToInt()}m"
    }
}