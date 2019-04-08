package ru.sis.statube.net.response.json.youtube.playListItem

import com.google.gson.annotations.SerializedName

class PlayListItemListResponse {

    @SerializedName("nextPageToken")
    var nextPageToken: String? = null

    @SerializedName("items")
    var items: List<PlayListItemResponse>? = null

}