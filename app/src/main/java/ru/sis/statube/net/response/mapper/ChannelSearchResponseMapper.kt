package ru.sis.statube.net.response.mapper

import ru.sis.statube.model.Channel
import ru.sis.statube.net.response.json.search.ChannelSearchResponse

class ChannelSearchResponseMapper {

    fun map(from: ChannelSearchResponse?): Channel? {
        if (from == null || from.kind != "youtube#searchResult" || from.id?.kind != "youtube#channel")
            return null
        val channel = Channel()
        channel.id = from.snippet?.channelId ?: return null
        channel.title = from.snippet?.channelTitle
        channel.description = from.snippet?.description
        channel.thumbnails = ThumbnailsResponseMapper().map(from.snippet?.thumbnails)
        return channel
    }

}