package cz.muni.fi.nofuzzmenu.dto.view

import java.net.URL

data class RestaurantInfoDto(
    var name: String,
    var address: String,
    var distance: Long,
    var web: URL //or string?
)