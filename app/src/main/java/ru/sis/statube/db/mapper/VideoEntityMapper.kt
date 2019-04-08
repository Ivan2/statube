package ru.sis.statube.db.mapper

import org.joda.time.DateTime
import ru.sis.statube.db.entity.VideoEntity
import ru.sis.statube.model.Video

class VideoEntityMapper {

    fun map(from: VideoEntity): Video? {
        val video = Video()
        video.id = from.id
        video.uploads = from.uploads ?: return null
        video.publishedAt = from.publishedAt?.let { DateTime(it) } ?: return null
        video.viewCount = from.viewCount
        video.likeCount = from.likeCount
        video.dislikeCount = from.dislikeCount
        video.commentCount = from.commentCount
        return video
    }

    fun reverseMap(from: Video): VideoEntity {
        val video = VideoEntity()
        video.id = from.id
        video.uploads = from.uploads
        video.publishedAt = from.publishedAt.millis
        video.viewCount = from.viewCount
        video.likeCount = from.likeCount
        video.dislikeCount = from.dislikeCount
        video.commentCount = from.commentCount
        return video
    }

}