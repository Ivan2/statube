package ru.sis.statube

import android.content.Context
import org.joda.time.DateTime

class Preferences {

    companion object {
        private var INSTANCE: Preferences? = null
        fun getInstance(): Preferences {
            val instance = INSTANCE ?: Preferences()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val appPreferencesName = "StaTube_preferences"

    private val statistics1LastUpdatedDateTimeKey = "STATISTICS1_LAST_UPDATED_DATE_TIME"
    private val statistics2LastUpdatedDateTimeKey = "STATISTICS2_LAST_UPDATED_DATE_TIME"


    fun setStatistics1LastUpdatedDateTime(context: Context, dateTime: DateTime?) = setDateTime(context, statistics1LastUpdatedDateTimeKey, dateTime)

    fun getStatistics1LastUpdatedDateTime(context: Context): DateTime? = getDateTime(context, statistics1LastUpdatedDateTimeKey)


    fun setStatistics2LastUpdatedDateTime(context: Context, dateTime: DateTime?) = setDateTime(context, statistics2LastUpdatedDateTimeKey, dateTime)

    fun getStatistics2LastUpdatedDateTime(context: Context): DateTime? = getDateTime(context, statistics2LastUpdatedDateTimeKey)


    protected fun setDateTime(context: Context, key: String, value: DateTime?) {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putLong(key, value?.millis ?: 0)
        editor.apply()
    }

    protected fun getDateTime(context: Context, key: String): DateTime? {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        val millis = pref.getLong(key, 0)
        return if (millis == 0L) null else DateTime(millis)
    }

}