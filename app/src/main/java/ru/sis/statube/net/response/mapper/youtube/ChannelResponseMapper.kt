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
        channel.thumbnail = from.snippet?.thumbnails?.let {
            it.high?.url ?: it.medium?.url ?: it.default?.url
        }
        channel.country = from.snippet?.country
        channel.viewCount = from.statistics?.viewCount?.toLongOrNull()
        channel.subscriberCount = from.statistics?.subscriberCount?.toLongOrNull()
        channel.videoCount = from.statistics?.videoCount?.toLongOrNull()
        channel.keywords = from.brandingSettings?.channel?.keywords
        channel.bannerImageUrl = from.brandingSettings?.image?.let {
            it.bannerMobileHdImageUrl ?: it.bannerMobileMediumHdImageUrl ?: it.bannerMobileExtraHdImageUrl ?:
            it.bannerMobileLowImageUrl ?: it.bannerMobileImageUrl
        }
        channel.uploads = from.contentDetails?.relatedPlayLists?.uploads
        return channel
    }

}