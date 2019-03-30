package ru.sis.statube.interactor

import android.content.Context
import com.google.gson.Gson
import ru.sis.statube.R
import ru.sis.statube.config.Config
import java.io.BufferedReader
import java.io.InputStreamReader

open class Interactor {

    protected fun loadConfig(context: Context): Config {
        return context.resources.openRawResource(R.raw.config).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                val json = reader.readText()
                Gson().fromJson(json, Config::class.java)
            }
        }
    }

}