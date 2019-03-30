package ru.sis.statube.interactor

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.joda.time.DateTime
import ru.sis.statube.additional.YOUTUBE_DATA_API_URL
import ru.sis.statube.model.Video
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.youtube.video.VideoListResponse
import ru.sis.statube.net.response.json.youtube.video.VideoSearchListResponse
import ru.sis.statube.net.response.json.youtube.video.VideoSearchResponse
import ru.sis.statube.net.response.mapper.youtube.VideoResponseMapper

class VideosInteractor : Interactor() {

    companion object {
        private var INSTANCE: VideosInteractor? = null
        fun getInstance(): VideosInteractor {
            val instance = INSTANCE ?: VideosInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val searchPath = "search?key=%s&channelId=%s&part=id&order=date&maxResults=50&type=video"
    private val searchWithTokenPath = "search?key=%s&channelId=%s&part=id&order=date&maxResults=50&type=video&pageToken=%s"
    private val videoPath = "videos?key=%s&id=%s&part=snippet,statistics"

    fun searchAsync(context: Context, channelId: String) = GlobalScope.async {
        val config = loadConfig(context)

        val beginDate = DateTime.now().minusMonths(2)

        val mapper = VideoResponseMapper()
        val videoList = ArrayList<Video>()

        var pageToken: String? = null

        while (true) {
            val searchUrl = if (pageToken == null)
                "$YOUTUBE_DATA_API_URL${String.format(searchPath, config.youtubeDataApiKey, channelId)}"
            else
                "$YOUTUBE_DATA_API_URL${String.format(searchWithTokenPath, config.youtubeDataApiKey, channelId, pageToken)}"
            val response = OkRequest.getInstance().get(searchUrl)
            val videoSearchListResponse = Gson().fromJson(response, VideoSearchListResponse::class.java)
            val items = videoSearchListResponse.items
            if (items != null)
                if (!loadVideos(config.youtubeDataApiKey, items, beginDate, mapper, videoList))
                    break

            pageToken = videoSearchListResponse.nextPageToken
            if (pageToken == null)
                break
        }

        videoList
    }

    private fun loadVideos(youtubeDataApiKey: String, videoSearchResponseList: List<VideoSearchResponse>,
                           beginDate: DateTime, mapper: VideoResponseMapper, videoList: ArrayList<Video>): Boolean {
        videoSearchResponseList.forEach { videoSearchResponse ->
            if (videoSearchResponse.kind == "youtube#searchResult" && videoSearchResponse.id?.kind == "youtube#video") {
                videoSearchResponse.id?.id?.let { id ->
                    val videoUrl = "$YOUTUBE_DATA_API_URL${String.format(videoPath, youtubeDataApiKey, id)}"
                    val response = OkRequest.getInstance().get(videoUrl)
                    val videoListResponse = Gson().fromJson(response, VideoListResponse::class.java)
                    videoListResponse.items?.forEach { videoResponse ->
                        mapper.map(videoResponse)?.let { video ->
                            if (video.publishedAt.isBefore(beginDate))
                                return false
                            videoList.add(video)
                        }
                    }
                }
            }
        }
        return true
    }

}