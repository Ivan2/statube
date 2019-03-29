package ru.sis.statube.net.response.json

import com.google.gson.annotations.SerializedName

class ThumbnailsResponse {

    @SerializedName("default")
    var default: ThumbnailResponse? = null

    @SerializedName("medium")
    var medium: ThumbnailResponse? = null

    @SerializedName("high")
    var high: ThumbnailResponse? = null

}