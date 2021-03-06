package cz.muni.fi.nofuzzmenu.utils

import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.dto.view.RestaurantInfoDto
import cz.muni.fi.nofuzzmenu.models.MenuItem
import cz.muni.fi.nofuzzmenu.models.Restaurant
import io.realm.RealmList

object Mapper {
    fun menuItemToDto(menuItem: MenuItem): MenuItemDto {
        return MenuItemDto(menuItem.menuId!!, menuItem.name!!, menuItem.cost ?: "")
    }

    fun dtoToMenuItem(dto: MenuItemDto): MenuItem {
        val menuItem = MenuItem()
        menuItem.menuId = dto.id
        menuItem.name = dto.name
        menuItem.cost = dto.cost
        return menuItem
    }

    fun restaurantToDto(restaurant: Restaurant): RestaurantInfoDto {
        return RestaurantInfoDto(
            id = restaurant.restaurantId!!,
            name = restaurant.name!!,
            address = restaurant.address!!,
            cuisines = restaurant.cuisines!!,
            distance = restaurant.distance!!,
            price_range = restaurant.priceRange!!,
            rating = restaurant.rating!!
        )
    }

    fun dtoToRestaurant(dto: RestaurantInfoDto): Restaurant {
        val r = Restaurant()
        r.distance = dto.distance
        r.address = dto.address
        r.cuisines = dto.cuisines
        r.restaurantId = dto.id
        r.name = dto.name
        r.priceRange = dto.price_range
        r.rating = dto.rating
        return r
    }

    fun menusToDtos(menu: RealmList<MenuItem>): MutableList<MenuItemDto> {
        val result = mutableListOf<MenuItemDto>()
        for (m in menu){
            result.add(menuItemToDto(m))
        }
        return result
    }

    fun restaurantsToDtoList(restaurants: RealmList<Restaurant>): MutableList<RestaurantInfoDto> {
        val result = mutableListOf<RestaurantInfoDto>()
        for (r in restaurants){
            result.add(restaurantToDto(r))
        }
        return result
    }
}