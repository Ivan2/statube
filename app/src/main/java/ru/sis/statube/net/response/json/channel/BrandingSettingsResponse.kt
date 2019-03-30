package ru.sis.statube.net.response.json.channel

import com.google.gson.annotations.SerializedName

class BrandingSettingsResponse {

    @SerializedName("channel")
    var channel: BrandingSettingsChannelResponse? = null

    @SerializedName("image")
    var image: BrandingSettingsImageResponse? = null

}