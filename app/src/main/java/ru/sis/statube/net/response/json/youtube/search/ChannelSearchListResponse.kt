package ru.sis.statube.net.response.json.youtube.search

import com.google.gson.annotations.SerializedName

class ChannelSearchListResponse {

    @SerializedName("nextPageToken")
    var nextPageToken: String? = null

    @SerializedName("items")
    var items: List<ChannelSearchResponse>? = null

}