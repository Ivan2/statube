package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeGrowthResponse {

    @SerializedName("subs")
    var subs: Double? = null

    @SerializedName("views")
    var views: Double? = null

}