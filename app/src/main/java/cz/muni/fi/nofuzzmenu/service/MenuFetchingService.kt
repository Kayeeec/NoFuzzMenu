package cz.muni.fi.nofuzzmenu.service

import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.model.settings.Settings

class MenuFetchingService(settings: Settings){

    fun fetchMenusPerRestaurant(restaurant: RestaurantInfoDto): List<MenuItemDto>{
        TODO() // just a draft
    }
}