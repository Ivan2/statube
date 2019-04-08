package ru.sis.statube.net.response.json.youtube.playListItem

import com.google.gson.annotations.SerializedName

class PlayListItemResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("contentDetails")
    var contentDetails: PlayListItemContentDetailsResponse? = null

}