package ru.sis.statube.db.store

import io.realm.Realm
import ru.sis.statube.db.entity.VideosStatisticsLastUpdatedEntity
import ru.sis.statube.db.mapper.VideosStatisticsLastUpdatedEntityMapper
import ru.sis.statube.model.VideosStatisticsLastUpdated

class VideosStatisticsLastUpdatedStore {

    companion object {
        private var INSTANCE: VideosStatisticsLastUpdatedStore? = null
        fun getInstance() = INSTANCE ?: VideosStatisticsLastUpdatedStore().apply { INSTANCE = this }
    }

    fun getVideosStatisticsLastUpdated(id: String): VideosStatisticsLastUpdated? = Realm.getDefaultInstance().use { realm ->
        val entity = realm.where(VideosStatisticsLastUpdatedEntity::class.java)
            .equalTo("id", id)
            .findFirst()
        entity?.let {
            VideosStatisticsLastUpdatedEntityMapper().map(entity)
        }
    }

    fun saveVideosStatisticsLastUpdated(statisticsLastUpdated: VideosStatisticsLastUpdated) = Realm.getDefaultInstance().use { realm ->
        val entity = VideosStatisticsLastUpdatedEntityMapper().reverseMap(statisticsLastUpdated)
        realm.executeTransaction {
            realm.insertOrUpdate(entity)
        }
        Unit
    }

}