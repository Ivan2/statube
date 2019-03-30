package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeDataResponse {

    @SerializedName("uploads")
    var uploads: Int? = null

    @SerializedName("subs")
    var subs: Int? = null

    @SerializedName("views")
    var views: Int? = null

    @SerializedName("avgdailysubs")
    var avgDailySubs: Int? = null

    @SerializedName("avgdailyviews")
    var avgDailyViews: Int? = null

}