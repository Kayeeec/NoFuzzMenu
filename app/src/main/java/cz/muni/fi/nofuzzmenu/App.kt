package cz.muni.fi.nofuzzmenu

import android.app.Application
import cz.muni.fi.nofuzzmenu.repository.RealmUtils

import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initializeRealm(this)

        RealmUtils.removeOldRequestsAsync()
    }

    private fun initializeRealm(context: App) {
        Realm.init(context)
        val realmConfig = RealmConfiguration.Builder()
            .name("nofuzzmenu.realm")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}
