package ru.sis.statube.interactor

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import org.joda.time.DateTime
import ru.sis.statube.additional.YOUTUBE_DATA_API_URL
import ru.sis.statube.additional.async
import ru.sis.statube.db.store.VideoStore
import ru.sis.statube.model.PlayListItem
import ru.sis.statube.model.Video
import ru.sis.statube.model.VideosStatisticsLastUpdated
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.youtube.playListItem.PlayListItemListResponse
import ru.sis.statube.net.response.json.youtube.video.VideoListResponse
import ru.sis.statube.net.response.mapper.youtube.PlayListItemResponseMapper
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

    private val playlistItemsPath = "playlistItems?key=%s&playlistId=%s&part=contentDetails&maxResults=50"
    private val playlistItemsWithTokenPath = "playlistItems?key=%s&playlistId=%s&part=contentDetails&maxResults=50&pageToken=%s"
    private val videoPath = "videos?key=%s&id=%s&part=snippet,statistics,contentDetails"
    private val fullVideoPath = "videos?key=%s&id=%s&part=snippet,statistics,contentDetails,liveStreamingDetails"

    suspend fun getVideosAsync(context: Context, uploads: String, beginDate: DateTime, endDate: DateTime, channelId: String) = async {
        val config = getConfig(context)
        val playListItems = getPlayListItems(uploads, beginDate, endDate, config.youtubeDataApiKey)
        val videoList = getVideos(playListItems, config.youtubeDataApiKey)
        VideoStore.getInstance().saveVideos(videoList)
        val statisticsLastUpdated = VideosStatisticsLastUpdated().apply {
            this.id = uploads
            this.date = DateTime.now()
            this.beginDate = beginDate
            this.endDate = endDate
        }
        StatisticsLastUpdatedInteractor.getInstance().setVideosStatisticsLastUpdatedAsync(statisticsLastUpdated).await()
        getVideosLocalAsync(channelId, beginDate, endDate).await()
    }

    private fun getPlayListItems(uploads: String, beginDate: DateTime, endDate: DateTime, youtubeDataApiKey: String): List<PlayListItem> {
        val beginDate2 = beginDate.minusMonths(1)
        val mapper = PlayListItemResponseMapper()
        val playListItems = ArrayList<PlayListItem>()
        var pageToken: String? = null

        while (true) {
            val playListItemsUrl = if (pageToken == null)
                "$YOUTUBE_DATA_API_URL${String.format(playlistItemsPath, youtubeDataApiKey, uploads)}"
            else
                "$YOUTUBE_DATA_API_URL${String.format(playlistItemsWithTokenPath, youtubeDataApiKey, uploads, pageToken)}"
            val response = OkRequest.getInstance().get(playListItemsUrl)
            val playListItemListResponse = Gson().fromJson(response, PlayListItemListResponse::class.java)
            if (playListItemListResponse.items.isNullOrEmpty())
                break

            var toStop = false
            playListItemListResponse.items?.forEach { playListItemResponse ->
                mapper.map(playListItemResponse)?.let { playListItem ->
                    if (playListItem.videoPublishedAt.isBefore(beginDate)) {
                        if (playListItem.videoPublishedAt.isBefore(beginDate2))
                            toStop = true
                    } else {
                        if (playListItem.videoPublishedAt.isBefore(endDate))
                            playListItems.add(playListItem)
                    }
                }
            }
            if (toStop)
                break

            pageToken = playListItemListResponse.nextPageToken
            if (pageToken == null)
                break
        }

        return playListItems
    }

    private suspend fun getVideos(playListItemList: List<PlayListItem>, youtubeDataApiKey: String): List<Video> {
        val mapper = VideoResponseMapper()
        val videos = ArrayList<Video>()
        val videoDeferredList = ArrayList<Deferred<Video?>>()

        playListItemList.forEach { playListItem ->
            try {
                videoDeferredList.add(getVideoAsync(mapper, playListItem, youtubeDataApiKey))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        videoDeferredList.forEach { deferred ->
            try {
                deferred.await()?.let { video ->
                    synchronized(videos) {
                        videos.add(video)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return videos
    }

    private fun getVideoAsync(mapper: VideoResponseMapper, playListItem: PlayListItem, youtubeDataApiKey: String) = async {
        val videoUrl = "$YOUTUBE_DATA_API_URL${String.format(videoPath, youtubeDataApiKey, playListItem.videoId)}"
        val response = OkRequest.getInstance().get(videoUrl)
        val videoListResponse = Gson().fromJson(response, VideoListResponse::class.java)
        videoListResponse.items?.forEach { videoResponse ->
            mapper.map(videoResponse)?.let { video ->
                video.publishedAt = playListItem.videoPublishedAt
                return@async video
            }
        }
        null
    }

    fun getFullVideoAsync(context: Context, videoId: String) = async {
        val config = getConfig(context)
        val videoUrl = "$YOUTUBE_DATA_API_URL${String.format(fullVideoPath, config.youtubeDataApiKey, videoId)}"
        val response = OkRequest.getInstance().get(videoUrl)
        val videoListResponse = Gson().fromJson(response, VideoListResponse::class.java)
        val mapper = VideoResponseMapper()
        videoListResponse.items?.forEach { videoResponse ->
            mapper.map(videoResponse)?.let { video ->
                return@async video
            }
        }
        null
    }

    fun getVideosLocalAsync(channelId: String, beginDate: DateTime, endDate: DateTime) = async {
        val videoList = VideoStore.getInstance().getVideosByChannelId(channelId)
        videoList.filter { video ->
            video.publishedAt.isAfter(beginDate) && video.publishedAt.isBefore(endDate)
        }
    }

}