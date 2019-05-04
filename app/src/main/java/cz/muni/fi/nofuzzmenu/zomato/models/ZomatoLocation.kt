package cz.muni.fi.nofuzzmenu.zomato.models

data class ZomatoLocation(val address: String,
                          val locality: String,
                          val city: String,
                          val city_id: Int,
                          val latitude: String,
                          val longitude: String,
                          val country_id: Int)