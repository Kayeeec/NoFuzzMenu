package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable

data class MenuItemDto(
    var number: Int? = null, // if menu item had a number, otherwise just order
    var name: String,
    var cost: Double
): Serializable