package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeDataResponse {

    @SerializedName("uploads")
    var uploads: String? = null

    @SerializedName("subs")
    var subs: String? = null

    @SerializedName("views")
    var views: String? = null

    @SerializedName("avgdailysubs")
    var avgDailySubs: String? = null

    @SerializedName("avgdailyviews")
    var avgDailyViews: String? = null

}