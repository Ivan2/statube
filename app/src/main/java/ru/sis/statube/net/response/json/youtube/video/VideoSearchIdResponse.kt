package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoSearchIdResponse {

    @SerializedName("videoId")
    var id: String? = null

    @SerializedName("kind")
    var kind: String? = null

}