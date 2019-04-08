package ru.sis.statube.net.response.json.youtube.playListItem

import com.google.gson.annotations.SerializedName

class PlayListItemContentDetailsResponse {

    @SerializedName("videoId")
    var videoId: String? = null

    @SerializedName("videoPublishedAt")
    var videoPublishedAt: String? = null

}