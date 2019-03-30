package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoStatisticsResponse {

    @SerializedName("viewCount")
    var viewCount: String? = null

    @SerializedName("likeCount")
    var likeCount: String? = null

    @SerializedName("dislikeCount")
    var dislikeCount: String? = null

    @SerializedName("commentCount")
    var commentCount: String? = null

}