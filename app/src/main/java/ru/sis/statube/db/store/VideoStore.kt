package ru.sis.statube.db.store

import io.realm.Realm
import ru.sis.statube.db.entity.VideoEntity
import ru.sis.statube.db.mapper.VideoEntityMapper
import ru.sis.statube.model.Video

class VideoStore {

    companion object {
        private var INSTANCE: VideoStore? = null
        fun getInstance() = INSTANCE ?: VideoStore().apply { INSTANCE = this }
    }

    fun getVideosByChannelId(channelId: String): List<Video> = Realm.getDefaultInstance().use { realm ->
        val videos = ArrayList<Video>()
        val entities = realm.where(VideoEntity::class.java)
            .equalTo("channelId", channelId)
            .findAll()
        val mapper = VideoEntityMapper()
        for (entity in entities) {
            mapper.map(entity)?.let { video ->
                videos.add(video)
            }
        }
        videos
    }

    fun saveVideos(videos: List<Video>) = Realm.getDefaultInstance().use { realm ->
        val mapper = VideoEntityMapper()
        val entities = ArrayList<VideoEntity>()
        videos.forEach { video ->
            entities.add(mapper.reverseMap(video))
        }
        realm.executeTransaction {
            realm.insertOrUpdate(entities)
        }
        Unit
    }

}