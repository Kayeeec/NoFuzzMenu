package cz.muni.fi.nofuzzmenu.zomato.models

data class ZomatoRestaurant(val id: String,
                            val name: String,
                            val url: String,
                            val location: ZomatoLocation,
                            val cuisines: String,
                            val menu_url: String)