package cz.muni.fi.nofuzzmenu.dto.view

data class RestaurantMenuDto(
    var menus: Set<MenuItemDto> = setOf(),
    var restaurant: RestaurantInfoDto
)

