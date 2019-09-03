package ru.sis.statube.net

import okhttp3.OkHttpClient
import okhttp3.Request
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

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    fun get(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        val str = response.body?.string()
        return str
    }

}
