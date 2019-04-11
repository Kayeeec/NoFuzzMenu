package cz.muni.fi.nofuzzmenu.service

import cz.muni.fi.nofuzzmenu.dto.view.RestaurantMenuDto
import cz.muni.fi.nofuzzmenu.model.settings.Settings

class MenuFetchingService(settings: Settings){

    fun fetchMenus(): List<RestaurantMenuDto>{
        TODO()
        // will need access to selected location, menus limit, radius etc. settings?
    }
}