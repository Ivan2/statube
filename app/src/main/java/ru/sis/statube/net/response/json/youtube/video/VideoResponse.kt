package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("statistics")
    var statistics: VideoStatisticsResponse? = null

}