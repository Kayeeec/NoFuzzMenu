package cz.muni.fi.nofuzzmenu.zomato.models

import java.io.Serializable

data class ZomatoRestaurant(val id: String,
                            val name: String,
                            val url: String,
                            val location: ZomatoLocation,
                            val cuisines: String,
                            val menu_url: String): Serializable

/*
{
            "restaurant": {
                "R": {
                    "res_id": 18834715
                },
                "apikey": "fba201f738abbed300423c42a0e7aea1",
                "id": "18834715",
                "name": "Batohi Restaurant",
                "url": "https://www.zomato.com/raebareli/batohi-restaurant-raebareli-locality?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1",
                "location": {
                    "address": "Khozanpur, Unchahar, Raebareli Locality, Raebareli",
                    "locality": "Raebareli Locality",
                    "city": "Raebareli",
                    "city_id": 11418,
                    "latitude": "26.2212520000",
                    "longitude": "81.2408030000",
                    "zipcode": "",
                    "country_id": 1,
                    "locality_verbose": "Raebareli Locality, Raebareli"
                },
                "switch_to_order_menu": 0,
                "cuisines": "North Indian",
                "average_cost_for_two": 0,
                "price_range": 1,
                "currency": "Rs.",
                "offers": [],
                "opentable_support": 0,
                "is_zomato_book_res": 0,
                "mezzo_provider": "OTHER",
                "is_book_form_web_view": 0,
                "book_form_web_view_url": "",
                "book_again_url": "",
                "thumb": "",
                "user_rating": {
                    "aggregate_rating": 0,
                    "rating_text": "Not rated",
                    "rating_color": "CBCBC8",
                    "votes": 0
                },
                "photos_url": "https://www.zomato.com/raebareli/batohi-restaurant-raebareli-locality/photos?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1#tabtop",
                "menu_url": "https://www.zomato.com/raebareli/batohi-restaurant-raebareli-locality/menu?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1&openSwipeBox=menu&showMinimal=1#tabtop",
                "featured_image": "",
                "has_online_delivery": 0,
                "is_delivering_now": 0,
                "include_bogo_offers": true,
                "deeplink": "zomato://restaurant/18834715",
                "is_table_reservation_supported": 0,
                "has_table_booking": 0,
                "events_url": "https://www.zomato.com/raebareli/batohi-restaurant-raebareli-locality/events#tabtop?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1",
                "establishment_types": []
            }
        },
 */