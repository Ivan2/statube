package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeGrowthResponse {

    @SerializedName("subs")
    var subs: String? = null

    @SerializedName("views")
    var views: String? = null

}