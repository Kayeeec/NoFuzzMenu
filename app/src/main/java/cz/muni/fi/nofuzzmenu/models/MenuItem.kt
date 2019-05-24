package cz.muni.fi.nofuzzmenu.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MenuItem: RealmObject() {
    @PrimaryKey
    var menuId: String? = null
    var name: String? = null
    var cost: String? = null
}