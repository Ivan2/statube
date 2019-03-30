package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.model.ChannelStatistics
import ru.sis.statube.net.response.json.youtube.channel.StatisticsResponse

class StatisticsResponseMapper {

    fun map(from: StatisticsResponse?): ChannelStatistics? {
        if (from == null)
            return null
        val statistics = ChannelStatistics()
        statistics.viewCount = from.viewCount
        statistics.subscriberCount = from.subscriberCount
        statistics.videoCount = from.videoCount
        return statistics
    }

}