package cz.muni.fi.nofuzzmenu.zomato.models

/**
 * JSON example:
 * --------------
{
    "daily_menus":[
            {
            "daily_menu":{
                "daily_menu_id":"20388858",
                "start_date":"2019-05-17 00:00:00",
                "end_date":"2019-05-17 23:59:59",
                "name":"",
                "dishes":[
                        {
                        "dish":{
                            "dish_id":"692282340",
                            "name":"Menu s pol\u00e9vkou",
                            "price":""
                            }
                        },
                        {
                        "dish":{
                            "dish_id":"692282341",
                            "name":"Frankfurtsk\u00e1 pol\u00e9vka s p\u00e1rkem a bramborem",
                            "price":""
                            }
                        }
                    ]
            }
        ],
    "status":"success"
}
 */

data class ZomatoDailyMenuResponse(
    var daily_menus: List<ZomatoMenu>?,
    var status: String
)

data class ZomatoMenu(
    var daily_menu: DailyMenu?
)

data class DailyMenu(
    var name: String,
    var start_date: String,
    var end_date: String,
    var dishes: List<Dish>?
)

data class Dish(
    var dish: DishDetails
)

data class DishDetails(
    var dish_id: String = "",
    var name: String = "",
    var price: String = ""
)