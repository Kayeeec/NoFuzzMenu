package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable
import java.text.NumberFormat
import java.util.*

data class RestaurantInfoDto(
    var id: String,
    var name: String, //ok
    var address: String, // ok
    val cuisines: String?,
    val distance: Float  // distance in meters
): Serializable {
    var distanceString: String = ""
     init {
         distanceString = convertDistanceToString(distance)
     }

    //todo what will be the maximum allowed radius?
    private fun convertDistanceToString(distance: Float): String {
        if (distance > 1000){
            val n = distance / 1000
            val fmtLocale = Locale.getDefault(Locale.Category.FORMAT) // todo: will need to get system locale?
            val formatter = NumberFormat.getInstance(fmtLocale)
            formatter.maximumFractionDigits = 1
            return "${formatter.format(n)} km"
        }
        return "$distance m"
    }
}