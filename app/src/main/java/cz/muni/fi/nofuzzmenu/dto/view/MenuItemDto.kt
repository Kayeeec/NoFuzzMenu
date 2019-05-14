package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable

data class MenuItemDto(
    var name: String,
    var cost: String // price with currency e.g. 89 Kč
): Serializable