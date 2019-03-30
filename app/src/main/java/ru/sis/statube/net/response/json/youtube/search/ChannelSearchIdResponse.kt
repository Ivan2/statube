package ru.sis.statube.net.response.json.youtube.search

import com.google.gson.annotations.SerializedName

class ChannelSearchIdResponse {

    @SerializedName("channelId")
    var id: String? = null

    @SerializedName("kind")
    var kind: String? = null

}