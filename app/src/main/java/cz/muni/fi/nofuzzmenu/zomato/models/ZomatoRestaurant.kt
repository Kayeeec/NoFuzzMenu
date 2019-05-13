package cz.muni.fi.nofuzzmenu.zomato.models

import java.io.Serializable

data class ZomatoRestaurant(@Transient val id: String,
                            val name: String,
                            val url: String,
                            val location: ZomatoLocation,
                            val cuisines: String,
                            val menu_url: String): Serializable