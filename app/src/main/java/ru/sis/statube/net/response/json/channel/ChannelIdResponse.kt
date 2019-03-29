package ru.sis.statube.net.response.json.channel

import com.google.gson.annotations.SerializedName

class ChannelIdResponse {

    @SerializedName("channelId")
    var id: String? = null

    @SerializedName("kind")
    var kind: String? = null

}