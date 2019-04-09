package ru.sis.statube.db.mapper

import org.joda.time.DateTime
import ru.sis.statube.db.entity.VideosStatisticsLastUpdatedEntity
import ru.sis.statube.model.VideosStatisticsLastUpdated

class VideosStatisticsLastUpdatedEntityMapper {

    fun map(from: VideosStatisticsLastUpdatedEntity): VideosStatisticsLastUpdated? {
        val lastUpdated = VideosStatisticsLastUpdated()
        lastUpdated.id = from.id
        lastUpdated.date = from.date?.let { DateTime(it) } ?: return null
        lastUpdated.beginDate = from.beginDate?.let { DateTime(it) } ?: return null
        lastUpdated.endDate = from.endDate?.let { DateTime(it) } ?: return null
        return lastUpdated
    }

    fun reverseMap(from: VideosStatisticsLastUpdated): VideosStatisticsLastUpdatedEntity {
        val lastUpdated = VideosStatisticsLastUpdatedEntity()
        lastUpdated.id = from.id
        lastUpdated.date = from.date.millis
        lastUpdated.beginDate = from.beginDate.millis
        lastUpdated.endDate = from.endDate.millis
        return lastUpdated
    }

}