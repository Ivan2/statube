package ru.sis.statube.db.store

import io.realm.Realm
import ru.sis.statube.db.entity.SocialBladeStatisticsEntity
import ru.sis.statube.db.mapper.SocialBladeStatisticsEntityMapper
import ru.sis.statube.model.SocialBladeStatistics

class SocialBladeStatisticsStore {

    companion object {
        private var INSTANCE: SocialBladeStatisticsStore? = null
        fun getInstance(): SocialBladeStatisticsStore {
            if (INSTANCE == null)
                INSTANCE = SocialBladeStatisticsStore()
            return INSTANCE!!
        }
    }

    fun getSocialBladeStatistics(channelId: String): SocialBladeStatistics? = Realm.getDefaultInstance().use { realm ->
        val entity = realm.where(SocialBladeStatisticsEntity::class.java)
            .equalTo("channelId", channelId)
            .findFirst()
        SocialBladeStatisticsEntityMapper().map(entity)
    }

    fun saveSocialBladeStatistics(channelId: String, statistics: SocialBladeStatistics) = Realm.getDefaultInstance().use { realm ->
        val entity = SocialBladeStatisticsEntityMapper().reverseMap(statistics)
        realm.executeTransaction {
            realm.insertOrUpdate(entity)
        }
        Unit
    }

}