package ru.sis.statube.db.store

import io.realm.Realm
import ru.sis.statube.db.entity.GeneralStatisticsLastUpdatedEntity
import ru.sis.statube.db.mapper.GeneralStatisticsLastUpdatedEntityMapper
import ru.sis.statube.model.GeneralStatisticsLastUpdated

class GeneralStatisticsLastUpdatedStore {

    companion object {
        private var INSTANCE: GeneralStatisticsLastUpdatedStore? = null
        fun getInstance() = INSTANCE ?: GeneralStatisticsLastUpdatedStore().apply { INSTANCE = this }
    }

    fun getGeneralStatisticsLastUpdated(id: String): GeneralStatisticsLastUpdated? = Realm.getDefaultInstance().use { realm ->
        val entity = realm.where(GeneralStatisticsLastUpdatedEntity::class.java)
            .equalTo("id", id)
            .findFirst()
        entity?.let {
            GeneralStatisticsLastUpdatedEntityMapper().map(entity)
        }
    }

    fun saveGeneralStatisticsLastUpdated(statisticsLastUpdated: GeneralStatisticsLastUpdated) = Realm.getDefaultInstance().use { realm ->
        val entity = GeneralStatisticsLastUpdatedEntityMapper().reverseMap(statisticsLastUpdated)
        realm.executeTransaction {
            realm.insertOrUpdate(entity)
        }
        Unit
    }

}