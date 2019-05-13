package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable

data class RestaurantInfoDto(
    var name: String, //ok
    var address: String, // ok
    val cuisines: String?,
    val distance: String
): Serializable