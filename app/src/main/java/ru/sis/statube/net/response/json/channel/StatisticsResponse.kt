package ru.sis.statube.net.response.json.channel

import com.google.gson.annotations.SerializedName

class StatisticsResponse {

    @SerializedName("viewCount")
    var viewCount: String? = null

    @SerializedName("subscriberCount")
    var subscriberCount: String? = null

    @SerializedName("videoCount")
    var videoCount: String? = null

}