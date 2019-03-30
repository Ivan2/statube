package ru.sis.statube.net.response.json.search

import com.google.gson.annotations.SerializedName
import ru.sis.statube.net.response.json.ChannelSnippetResponse

class ChannelSearchResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: ChannelSearchIdResponse? = null

    @SerializedName("snippet")
    var snippet: ChannelSnippetResponse? = null

}