package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeViewsResponse {

    @SerializedName("views14")
    var views14: Int? = null

    @SerializedName("views30")
    var views30: Int? = null

    @SerializedName("views60")
    var views60: Int? = null

    @SerializedName("views90")
    var views90: Int? = null

    @SerializedName("views180")
    var views180: Int? = null

    @SerializedName("views365")
    var views365: Int? = null

}