package ru.sis.statube.db.store

import io.realm.Realm
import ru.sis.statube.db.entity.StatisticsLastUpdatedEntity
import ru.sis.statube.db.mapper.StatisticsLastUpdatedEntityMapper
import ru.sis.statube.model.StatisticsLastUpdated

class StatisticsLastUpdatedStore {

    companion object {
        private var INSTANCE: StatisticsLastUpdatedStore? = null
        fun getInstance(): StatisticsLastUpdatedStore {
            if (INSTANCE == null)
                INSTANCE = StatisticsLastUpdatedStore()
            return INSTANCE!!
        }
    }

    fun getStatisticsLastUpdated(id: String): StatisticsLastUpdated? = Realm.getDefaultInstance().use { realm ->
        val entity = realm.where(StatisticsLastUpdatedEntity::class.java)
            .equalTo("id", id)
            .findFirst()
        entity?.let {
            StatisticsLastUpdatedEntityMapper().map(entity)
        }
    }

    fun saveStatisticsLastUpdated(statisticsLastUpdated: StatisticsLastUpdated) = Realm.getDefaultInstance().use { realm ->
        val entity = StatisticsLastUpdatedEntityMapper().reverseMap(statisticsLastUpdated)
        realm.executeTransaction {
            realm.insertOrUpdate(entity)
        }
        Unit
    }

}