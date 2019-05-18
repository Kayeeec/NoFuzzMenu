package cz.muni.fi.nofuzzmenu.repository

import cz.muni.fi.nofuzzmenu.dto.view.MenuItemDto
import cz.muni.fi.nofuzzmenu.zomato.ZomatoApi
import cz.muni.fi.nofuzzmenu.zomato.models.DailyMenu
import cz.muni.fi.nofuzzmenu.zomato.models.ZomatoMenu
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DailyMenuRepository() : BaseRepository() {
    private val zomatoApi = ZomatoApi("fba201f738abbed300423c42a0e7aea1") //todo api key storage

    suspend fun getMenu(restaurantId: String): MutableList<MenuItemDto> {

//        val call = zomatoApi.service.getMenuAsync(apiKey = zomatoApi.apiKey, restaurantId = "16506939") //working menu display
        val call = zomatoApi.service.getMenuAsync(apiKey = zomatoApi.apiKey, restaurantId = restaurantId)

        val response = safeApiCall(
            call = { call.await() },
            errorMessage = "Error fetching restaurant's daily menu from zomato."
        )
        val result = mutableListOf<MenuItemDto>()
        response?.daily_menus?.forEach { zomatoMenu: ZomatoMenu ->
            val dailyMenu = zomatoMenu.daily_menu ?: return@forEach
            if (isTodaysMenu(dailyMenu)) {
                dailyMenu.dishes?.forEach {
                    val dish = it.dish
                    if (dish.name.isNotEmpty()){
                        result.add(MenuItemDto(name = dish.name, cost = dish.price))
                    }
                }
            }
        }
        return result
    }

    private fun isTodaysMenu(dailyMenu: DailyMenu): Boolean {
        val startDate = dateFromString(dailyMenu.start_date)
        val endDate = dateFromString(dailyMenu.end_date)
        val today = LocalDate.now()

        if (startDate != null && endDate != null) {
            return today in startDate..endDate
        }
        if (startDate == null && endDate == null){
            return true // todo what to return? just include in the list? failed to parse? probably won't be null tho
        }
        if (startDate != null) return today.isEqual(startDate)
        return today.isEqual(endDate)
    }

    private fun dateFromString(string: String): LocalDate? {
        val fmtLocale = Locale.getDefault(Locale.Category.FORMAT)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", fmtLocale)
        try {
            return dateFormat.parse(string).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

}