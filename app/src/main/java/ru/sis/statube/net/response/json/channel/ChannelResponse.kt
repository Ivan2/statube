package ru.sis.statube.net.response.json.channel

import com.google.gson.annotations.SerializedName
import ru.sis.statube.net.response.json.ChannelSnippetResponse

class ChannelResponse {

    @SerializedName("kind")
    var kind: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("snippet")
    var snippet: ChannelSnippetResponse? = null

    @SerializedName("statistics")
    var statistics: StatisticsResponse? = null

    @SerializedName("brandingSettings")
    var brandingSettings: BrandingSettingsResponse? = null

}