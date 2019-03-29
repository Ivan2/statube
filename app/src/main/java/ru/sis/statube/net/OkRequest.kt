package ru.sis.statube.net

import android.util.Log
import okhttp3.*
import ru.sis.statube.BuildConfig
import java.util.concurrent.TimeUnit

class OkRequest private constructor() {

    companion object {
        private var INSTANCE: OkRequest? = null
        fun getInstance(): OkRequest {
            val instance = INSTANCE ?: OkRequest()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    //private val mediaType = MediaType.parse("application/json; charset=utf-8")

    fun get(url: String): String? {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        val str = response.body()?.string()
        if (BuildConfig.DEBUG)
            Log.wtf("Response", str ?: "null")
        return str
    }

}
