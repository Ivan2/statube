package ru.sis.statube.net

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.sis.statube.exception.ResponseException
import ru.sis.statube.net.response.json.BaseResponse
import java.util.concurrent.TimeUnit

class OkRequest private constructor() {

    companion object {
        private var INSTANCE: OkRequest? = null
        fun getInstance() = INSTANCE ?: OkRequest().apply { INSTANCE = this }
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
        val baseResponse = Gson().fromJson(str, BaseResponse::class.java)
        val errorCode = baseResponse.error?.code
        val errorMessage = baseResponse.error?.message
        if (!errorCode.isNullOrEmpty() || !errorMessage.isNullOrEmpty())
            throw ResponseException(errorCode, errorMessage)
        return str
    }

}
