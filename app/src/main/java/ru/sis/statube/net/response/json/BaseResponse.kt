package ru.sis.statube.net.response.json

import com.google.gson.annotations.SerializedName

class BaseResponse {

    @SerializedName("error")
    var error: ErrorResponse? = null

}