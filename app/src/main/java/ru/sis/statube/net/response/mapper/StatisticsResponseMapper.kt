package ru.sis.statube.net.response.mapper

import ru.sis.statube.model.Statistics
import ru.sis.statube.net.response.json.channel.StatisticsResponse

class StatisticsResponseMapper {

    fun map(from: StatisticsResponse?): Statistics? {
        if (from == null)
            return null
        val statistics = Statistics()
        statistics.viewCount = from.viewCount
        statistics.subscriberCount = from.subscriberCount
        statistics.videoCount = from.videoCount
        return statistics
    }

}