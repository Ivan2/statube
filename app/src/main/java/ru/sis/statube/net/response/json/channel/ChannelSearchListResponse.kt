package ru.sis.statube.net.response.json.channel

import com.google.gson.annotations.SerializedName

class ChannelSearchListResponse {

    @SerializedName("nextPageToken")
    var nextPageToken: String? = null

    @SerializedName("items")
    var items: List<ChannelSearchResponse>? = null

}