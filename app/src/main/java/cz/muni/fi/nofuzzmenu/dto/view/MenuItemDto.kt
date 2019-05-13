package cz.muni.fi.nofuzzmenu.dto.view

import java.io.Serializable

data class MenuItemDto(
    var name: String,
    var cost: Double
): Serializable