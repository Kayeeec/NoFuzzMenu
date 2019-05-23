package cz.muni.fi.nofuzzmenu.models

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Information about what request the saved data comes from.
 * For deciding whether to load data from DB or fetch from the API.
 */
open class Request: RealmObject() {
    var longitude: Double? = null
    var latitude: Double? = null
    var radius: Double? = null
    var date: String? = null //YYYY-MM-DD
    var lastStart: Int? = null
    var lastCount: Int? = null
    var restaurants: RealmList<Restaurant> = RealmList()
}