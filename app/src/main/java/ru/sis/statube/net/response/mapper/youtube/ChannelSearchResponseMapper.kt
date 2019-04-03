package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.model.Channel
import ru.sis.statube.net.response.json.youtube.search.ChannelSearchResponse

class ChannelSearchResponseMapper {

    fun map(from: ChannelSearchResponse?): Channel? {
        if (from == null || from.kind != "youtube#searchResult" || from.id?.kind != "youtube#channel")
            return null
        val channel = Channel()
        channel.id = from.snippet?.channelId ?: return null
        channel.title = from.snippet?.channelTitle
        channel.description = from.snippet?.description
        channel.thumbnail = from.snippet?.thumbnails?.let {
            it.high?.url ?: it.medium?.url ?: it.default?.url
        }
        return channel
    }

}