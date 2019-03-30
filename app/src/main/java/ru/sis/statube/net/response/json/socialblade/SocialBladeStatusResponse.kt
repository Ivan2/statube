package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeStatusResponse {

    @SerializedName("response")
    var response: Int? = null

    @SerializedName("error")
    var error: Boolean? = null

}