package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeDataDailyResponse {

    @SerializedName("date")
    var date: String? = null

    @SerializedName("subs")
    var subs: Int? = null

    @SerializedName("views")
    var views: Int? = null

}