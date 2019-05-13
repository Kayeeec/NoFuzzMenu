package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable

data class RestaurantMenuDto(
    var menus: Set<MenuItemDto> = setOf(),
    var restaurant: RestaurantInfoDto? = null
) : Serializable

