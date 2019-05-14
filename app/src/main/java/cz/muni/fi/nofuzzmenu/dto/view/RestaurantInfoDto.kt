package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable

data class RestaurantInfoDto(
    var name: String, //ok
    var address: String, // ok
    // TODO: add calculation, can't do directly
    // var distance: Long, //TODO: nok
    val cuisines: String?
): Serializable