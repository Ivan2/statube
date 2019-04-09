package ru.sis.statube.db.mapper

import org.joda.time.DateTime
import ru.sis.statube.db.entity.GeneralStatisticsLastUpdatedEntity
import ru.sis.statube.model.GeneralStatisticsLastUpdated

class GeneralStatisticsLastUpdatedEntityMapper {

    fun map(from: GeneralStatisticsLastUpdatedEntity): GeneralStatisticsLastUpdated {
        val lastUpdated = GeneralStatisticsLastUpdated()
        lastUpdated.id = from.id
        lastUpdated.date = from.date?.let { DateTime(it) }
        return lastUpdated
    }

    fun reverseMap(from: GeneralStatisticsLastUpdated): GeneralStatisticsLastUpdatedEntity {
        val lastUpdated = GeneralStatisticsLastUpdatedEntity()
        lastUpdated.id = from.id
        lastUpdated.date = from.date?.millis
        return lastUpdated
    }

}