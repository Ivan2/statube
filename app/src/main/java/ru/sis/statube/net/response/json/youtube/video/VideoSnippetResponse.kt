package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName

class VideoSnippetResponse {

    @SerializedName("publishedAt")
    var publishedAt: String? = null

    @SerializedName("title")
    var title: String? = null

}