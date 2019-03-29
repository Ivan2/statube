package ru.sis.statube

import android.app.Application
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.RealmLog
import ru.sis.statube.additional.REALM_SCHEMA_VERSION

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(Log.DEBUG)
        }
        val realmConfiguration = RealmConfiguration.Builder()
                .schemaVersion(REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

}