package cz.muni.fi.nofuzzmenu.model.settings

data class Settings(
    var location: String, //depends on APIs analysis - how to tell them location
    var radius: Int,
    var menuLimit: Int
)