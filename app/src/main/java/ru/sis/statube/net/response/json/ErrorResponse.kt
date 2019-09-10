package ru.sis.statube.net.response.json

import com.google.gson.annotations.SerializedName

class ErrorResponse {

    @SerializedName("code")
    var code: String? = null

    @SerializedName("message")
    var message: String? = null

}