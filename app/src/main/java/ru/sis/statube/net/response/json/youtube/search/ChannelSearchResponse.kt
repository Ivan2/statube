package ru.sis.statube.net.response.json.youtube.search

import com.google.gson.annotations.SerializedName
import ru.sis.statube.net.response.json.youtube.ChannelSnippetResponse

class ChannelSearchResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: ChannelSearchIdResponse? = null

    @SerializedName("snippet")
    var snippet: ChannelSnippetResponse? = null

}