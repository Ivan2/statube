package ru.sis.statube.net.response.json.youtube.channel

import com.google.gson.annotations.SerializedName

class ChannelListResponse {

    @SerializedName("items")
    var items: List<ChannelResponse>? = null

}