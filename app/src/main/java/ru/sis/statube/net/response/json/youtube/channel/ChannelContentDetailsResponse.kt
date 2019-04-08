package ru.sis.statube.net.response.json.youtube.channel

import com.google.gson.annotations.SerializedName

class ChannelContentDetailsResponse {

    @SerializedName("relatedPlaylists")
    var relatedPlayLists: RelatedPlayListsResponse? = null

}