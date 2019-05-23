package cz.muni.fi.nofuzzmenu.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Restaurant: RealmObject() {
    @PrimaryKey
    var restaurantId: String? = null
    var name: String? = null
    var address: String? = null
    var cuisines: String? = null
    var distance: Float? = null
    var menu: RealmList<MenuItem> = RealmList()
}