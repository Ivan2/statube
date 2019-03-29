package ru.sis.statube.net.response.json.channel

import com.google.gson.annotations.SerializedName

class ChannelSearchResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: ChannelIdResponse? = null

    @SerializedName("snippet")
    var snippet: ChannelSnippetResponse? = null

}