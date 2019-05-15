package ru.sis.statube.presentation.screens.videos

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.StatisticsLastUpdatedInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.Video
import ru.sis.statube.model.VideosStatisticsLastUpdated
import ru.sis.statube.presentation.Presenter

class VideosPresenter : Presenter() {

    fun loadVideosLastUpdatedDateTime(uploads: String, onLoad: (lastUpdated: VideosStatisticsLastUpdated?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getVideosStatisticsLastUpdatedAsync(uploads).await()
        onLoad(statisticsLastUpdated)
    }, {
        onLoad(null)
    })

    fun loadVideos(context: Context, uploads: String, beginDate: DateTime, endDate: DateTime, channelId: String,
                   onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        val videoList = VideosInteractor.getInstance().getVideosAsync(context, uploads, beginDate, endDate, channelId).await()
        onLoad(videoList)
    }, {
        onLoad(null)
    })

    fun loadVideosLocal(channelId: String, beginDate: DateTime, endDate: DateTime,
                                  onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        val videoList = VideosInteractor.getInstance().getVideosLocalAsync(channelId, beginDate, endDate).await()
        onLoad(videoList)
    }, {
        onLoad(null)
    })

    fun sortVideos(videoList: ArrayList<Video>, sortMode: SortMode, sortDirection: Int, onSorted: () -> Unit) = resolvedLaunch({
        videoList.sortWith(Comparator { o1, o2 ->
            var param1: Long? = null
            var param2: Long? = null
            when (sortMode) {
                SortMode.DATE -> {
                    param1 = o1.publishedAt.millis
                    param2 = o2.publishedAt.millis
                }
                SortMode.VIEWS -> {
                    param1 = o1.viewCount
                    param2 = o2.viewCount
                }
                SortMode.LIKES -> {
                    param1 = o1.likeCount
                    param2 = o2.likeCount
                }
                SortMode.DISLIKES -> {
                    param1 = o1.dislikeCount
                    param2 = o2.dislikeCount
                }
                SortMode.COMMENTS -> {
                    param1 = o1.commentCount
                    param2 = o2.commentCount
                }
                SortMode.LIKES_DISLIKES -> {
                    param1 = o1.likeCount?.let { likeCount -> o1.dislikeCount?.let { dislikeCount ->
                        (likeCount * 1000000000f / dislikeCount).toLong()
                    } }
                    param2 = o2.likeCount?.let { likeCount -> o2.dislikeCount?.let { dislikeCount ->
                        (likeCount * 1000000000f / dislikeCount).toLong()
                    } }
                }
            }

            if (param1 == null && param2 == null)
                return@Comparator 0
            if (param1 == null)
                return@Comparator 1
            if (param2 == null)
                return@Comparator -1
            if (sortDirection == 0)
                param1.compareTo(param2)
            else
                param2.compareTo(param1)
        })
        onSorted()
    }, {
        onSorted()
    })

}
