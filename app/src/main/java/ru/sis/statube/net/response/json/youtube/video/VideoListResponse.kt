package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoListResponse {

    @SerializedName("items")
    var items: List<VideoResponse>? = null

}