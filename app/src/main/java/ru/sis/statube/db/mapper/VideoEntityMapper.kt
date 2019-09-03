package ru.sis.statube.db.mapper

import org.joda.time.DateTime
import org.joda.time.Period
import ru.sis.statube.db.entity.VideoEntity
import ru.sis.statube.model.Video

class VideoEntityMapper {

    fun map(from: VideoEntity): Video? {
        val video = Video()
        video.id = from.id
        video.channelId = from.channelId ?: return null
        video.publishedAt = from.publishedAt?.let { DateTime(it) } ?: return null
        video.title = from.title
        video.description = from.description
        video.thumbnail = from.thumbnail
        video.duration = from.duration?.let { Period.parse(it) }
        video.viewCount = from.viewCount
        video.likeCount = from.likeCount
        video.dislikeCount = from.dislikeCount
        video.commentCount = from.commentCount
        return video
    }

    fun reverseMap(from: Video): VideoEntity {
        val video = VideoEntity()
        video.id = from.id
        video.channelId = from.channelId
        video.publishedAt = from.publishedAt.millis
        video.title = from.title
        video.description = from.description
        video.thumbnail = from.thumbnail
        video.duration = from.duration?.toString()
        video.viewCount = from.viewCount
        video.likeCount = from.likeCount
        video.dislikeCount = from.dislikeCount
        video.commentCount = from.commentCount
        return video
    }

}