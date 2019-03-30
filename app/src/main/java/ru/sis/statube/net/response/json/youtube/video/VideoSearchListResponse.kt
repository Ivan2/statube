package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoSearchListResponse {

    @SerializedName("nextPageToken")
    var nextPageToken: String? = null

    @SerializedName("items")
    var items: List<VideoSearchResponse>? = null

}