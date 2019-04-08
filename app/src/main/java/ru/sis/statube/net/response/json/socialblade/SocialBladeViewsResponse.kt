package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeViewsResponse {

    @SerializedName("views14")
    var views14: String? = null

    @SerializedName("views30")
    var views30: String? = null

    @SerializedName("views60")
    var views60: String? = null

    @SerializedName("views90")
    var views90: String? = null

    @SerializedName("views180")
    var views180: String? = null

    @SerializedName("views365")
    var views365: String? = null

}