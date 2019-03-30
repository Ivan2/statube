package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeChartsResponse {

    @SerializedName("subs")
    var subs: SocialBladeSubsResponse? = null

    @SerializedName("views")
    var views: SocialBladeViewsResponse? = null

    @SerializedName("growth")
    var growth: SocialBladeGrowthResponse? = null

}