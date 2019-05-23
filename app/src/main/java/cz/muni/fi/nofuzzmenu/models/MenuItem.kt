package cz.muni.fi.nofuzzmenu.models

import io.realm.RealmObject

open class MenuItem: RealmObject() {
    var name: String? = null
    var cost: String? = null
}