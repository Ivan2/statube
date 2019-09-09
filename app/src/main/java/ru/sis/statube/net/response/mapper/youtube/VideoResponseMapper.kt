package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.additional.parseYoutubeDateTime
import ru.sis.statube.additional.parseYoutubePeriod
import ru.sis.statube.model.Video
import ru.sis.statube.net.response.json.youtube.video.VideoResponse

class VideoResponseMapper {

    fun map(from: VideoResponse?): Video? {
        if (from == null || from.kind != "youtube#video")
            return null
        val video = Video()
        video.id = from.id ?: return null
        video.publishedAt = from.snippet?.publishedAt?.parseYoutubeDateTime() ?: return null
        video.channelId = from.snippet?.channelId ?: return null
        video.title = from.snippet?.title
        video.description = from.snippet?.description
        video.thumbnail = from.snippet?.thumbnails?.let {
            it.high?.url ?: it.medium?.url ?: it.default?.url
        }
        video.tags = from.snippet?.tags
        video.duration = from.contentDetails?.duration?.parseYoutubePeriod()
        video.viewCount = from.statistics?.viewCount?.toLongOrNull()
        video.likeCount = from.statistics?.likeCount?.toLongOrNull()
        video.dislikeCount = from.statistics?.dislikeCount?.toLongOrNull()
        video.commentCount = from.statistics?.commentCount?.toLongOrNull()
        video.scheduledStartTime = from.liveStreamingDetails?.scheduledStartTime?.parseYoutubeDateTime()
        video.actualStartTime = from.liveStreamingDetails?.actualStartTime?.parseYoutubeDateTime()
        video.actualEndTime = from.liveStreamingDetails?.actualEndTime?.parseYoutubeDateTime()
        return video
    }

}