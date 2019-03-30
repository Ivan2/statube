package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.additional.parseYoutubeDateTime
import ru.sis.statube.model.Channel
import ru.sis.statube.net.response.json.youtube.channel.ChannelResponse

class ChannelResponseMapper {

    fun map(from: ChannelResponse?): Channel? {
        if (from == null || from.kind != "youtube#channel")
            return null
        val channel = Channel()
        channel.id = from.id ?: return null
        channel.publishedAt = from.snippet?.publishedAt?.parseYoutubeDateTime()
        channel.title = from.snippet?.title
        channel.description = from.snippet?.description
        channel.thumbnails = ThumbnailsResponseMapper().map(from.snippet?.thumbnails)
        channel.country = from.snippet?.country
        channel.statistics = StatisticsResponseMapper().map(from.statistics)
        channel.brandingSettings = BrandingSettingsResponseMapper().map(from.brandingSettings)
        return channel
    }

}