package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.model.Video
import ru.sis.statube.net.response.json.youtube.video.VideoResponse

class VideoResponseMapper {

    fun map(from: VideoResponse?): Video? {
        if (from == null || from.kind != "youtube#video")
            return null
        val video = Video()
        video.id = from.id ?: return null
        video.viewCount = from.statistics?.viewCount?.toLongOrNull()
        video.likeCount = from.statistics?.likeCount?.toLongOrNull()
        video.dislikeCount = from.statistics?.dislikeCount?.toLongOrNull()
        video.commentCount = from.statistics?.commentCount?.toLongOrNull()
        return video
    }

}