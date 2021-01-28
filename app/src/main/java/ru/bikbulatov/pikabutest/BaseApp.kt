package ru.bikbulatov.pikabutest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Realm.getDefaultConfiguration() == null) {
            Realm.init(this)
            val config = RealmConfiguration.Builder()
                .name("pikabu.realm")
                .schemaVersion(1)
                .build()
            Realm.setDefaultConfiguration(config)
        }
    }

    fun getRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}