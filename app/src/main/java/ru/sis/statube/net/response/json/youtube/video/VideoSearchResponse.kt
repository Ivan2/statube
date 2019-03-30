package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoSearchResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: VideoSearchIdResponse? = null

}