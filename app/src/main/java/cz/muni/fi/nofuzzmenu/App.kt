package cz.muni.fi.nofuzzmenu

import android.app.Application

import io.realm.Realm

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Realm.init(this)
    }
}
