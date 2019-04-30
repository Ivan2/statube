package ru.sis.statube.net.response.json.youtube.video

import com.google.gson.annotations.SerializedName
import ru.sis.statube.net.response.json.youtube.ThumbnailsResponse

class VideoSnippetResponse {

    @SerializedName("publishedAt")
    var publishedAt: String? = null

    @SerializedName("channelId")
    var channelId: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("thumbnails")
    var thumbnails: ThumbnailsResponse? = null

}