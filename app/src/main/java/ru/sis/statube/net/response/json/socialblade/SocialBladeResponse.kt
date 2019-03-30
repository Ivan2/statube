package ru.sis.statube.net.response.json.socialblade

import com.google.gson.annotations.SerializedName

class SocialBladeResponse {

    @SerializedName("status")
    var status: SocialBladeStatusResponse? = null

    @SerializedName("id")
    var id: SocialBladeIdResponse? = null

    @SerializedName("data")
    var data: SocialBladeDataResponse? = null

    @SerializedName("charts")
    var charts: SocialBladeChartsResponse? = null

    @SerializedName("data_daily")
    var dataDailyArray: Array<SocialBladeDataDailyResponse>? = null

}