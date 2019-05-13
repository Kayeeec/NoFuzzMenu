package cz.muni.fi.nofuzzmenu.zomato.models

data class ZomatoRestaurantsListResponse(val results_found : String,
                                         val results_start: Int,
                                         val results_shown : Int,
                                         val restaurants: List<ZomatoRestaurantWrapper>)