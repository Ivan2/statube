package ru.sis.statube.net.response.json.youtube

import com.google.gson.annotations.SerializedName

class ChannelSnippetResponse {

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

    @SerializedName("channelTitle")
    var channelTitle: String? = null

    @SerializedName("liveBroadcastContent")
    var liveBroadcastContent: String? = null

    @SerializedName("country")
    var country: String? = null

}