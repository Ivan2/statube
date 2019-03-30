package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.additional.parseYoutubeDateTime
import ru.sis.statube.model.Video
import ru.sis.statube.net.response.json.youtube.video.VideoResponse

class VideoResponseMapper {

    fun map(from: VideoResponse?): Video? {
        if (from == null || from.kind != "youtube#video")
            return null
        val video = Video()
        video.id = from.id ?: return null
        video.publishedAt = from.snippet?.publishedAt?.parseYoutubeDateTime() ?: return null
        video.title = from.snippet?.title
        video.viewCount = from.statistics?.viewCount?.toIntOrNull()
        video.likeCount = from.statistics?.likeCount?.toIntOrNull()
        video.dislikeCount = from.statistics?.dislikeCount?.toIntOrNull()
        video.commentCount = from.statistics?.commentCount?.toIntOrNull()
        return video
    }

}