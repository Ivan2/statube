package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoLiveStreamingDetailsResponse {

    @SerializedName("scheduledStartTime")
    var scheduledStartTime: String? = null

    @SerializedName("actualStartTime")
    var actualStartTime: String? = null

    @SerializedName("actualEndTime")
    var actualEndTime: String? = null

}