package ru.sis.statube.db.store

import io.realm.Realm
import ru.sis.statube.db.entity.GeneralStatisticsEntity
import ru.sis.statube.db.mapper.GeneralStatisticsEntityMapper
import ru.sis.statube.model.GeneralStatistics

class GeneralStatisticsStore {

    companion object {
        private var INSTANCE: GeneralStatisticsStore? = null
        fun getInstance(): GeneralStatisticsStore {
            if (INSTANCE == null)
                INSTANCE = GeneralStatisticsStore()
            return INSTANCE!!
        }
    }

    fun getGeneralStatistics(channelId: String): GeneralStatistics? = Realm.getDefaultInstance().use { realm ->
        val entity = realm.where(GeneralStatisticsEntity::class.java)
            .equalTo("channelId", channelId)
            .findFirst()
        GeneralStatisticsEntityMapper().map(entity)
    }

    fun saveGeneralStatistics(statistics: GeneralStatistics) = Realm.getDefaultInstance().use { realm ->
        val entity = GeneralStatisticsEntityMapper().reverseMap(statistics)
        realm.executeTransaction {
            realm.insertOrUpdate(entity)
        }
        Unit
    }

}