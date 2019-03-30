package ru.sis.statube.net.response.mapper

import org.joda.time.DateTime
import ru.sis.statube.model.Channel
import ru.sis.statube.net.response.json.channel.ChannelResponse

class ChannelResponseMapper {

    fun map(from: ChannelResponse?): Channel? {
        if (from == null || from.kind != "youtube#channel")
            return null
        val channel = Channel()
        channel.id = from.id ?: return null
        channel.publishedAt = from.snippet?.publishedAt?.let { publishedAt -> DateTime(publishedAt) }
        channel.title = from.snippet?.title
        channel.description = from.snippet?.description
        channel.thumbnails = ThumbnailsResponseMapper().map(from.snippet?.thumbnails)
        channel.country = from.snippet?.country
        channel.statistics = StatisticsResponseMapper().map(from.statistics)
        channel.brandingSettings = BrandingSettingsResponseMapper().map(from.brandingSettings)
        return channel
    }

}