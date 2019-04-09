package ru.sis.statube.db.mapper

import org.joda.time.DateTime
import ru.sis.statube.db.entity.StatisticsLastUpdatedEntity
import ru.sis.statube.model.StatisticsLastUpdated

class StatisticsLastUpdatedEntityMapper {

    fun map(from: StatisticsLastUpdatedEntity): StatisticsLastUpdated {
        val lastUpdated = StatisticsLastUpdated()
        lastUpdated.id = from.id
        lastUpdated.date = from.date?.let { DateTime(it) }
        return lastUpdated
    }

    fun reverseMap(from: StatisticsLastUpdated): StatisticsLastUpdatedEntity {
        val lastUpdated = StatisticsLastUpdatedEntity()
        lastUpdated.id = from.id
        lastUpdated.date = from.date?.millis
        return lastUpdated
    }

}