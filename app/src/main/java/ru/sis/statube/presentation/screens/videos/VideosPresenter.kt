package ru.sis.statube.presentation.screens.videos

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.additional.launch
import ru.sis.statube.interactor.StatisticsLastUpdatedInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.Video
import ru.sis.statube.model.VideosStatisticsLastUpdated
import ru.sis.statube.presentation.presenter.FullVideoLoadingPresenter
import kotlin.math.max
import kotlin.math.sqrt

class VideosPresenter : FullVideoLoadingPresenter() {

    fun loadVideosLastUpdatedDateTime(context: Context, uploads: String,
            onLoad: (lastUpdated: VideosStatisticsLastUpdated?) -> Unit) = launch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getVideosStatisticsLastUpdatedAsync(uploads).await()
        onLoad(statisticsLastUpdated)
    }, { e ->
        onLoad(null)
        onError(context, e)
    })

    fun loadVideos(context: Context, uploads: String, beginDate: DateTime, endDate: DateTime,
            channelId: String, onLoad: (videoList: List<Video>) -> Unit) = launch({
        val videoList = VideosInteractor.getInstance().getVideosAsync(context, uploads, beginDate, endDate, channelId).await()
        onLoad(videoList)
    }, { e ->
        onLoad(emptyList())
        onError(context, e)
    })

    fun loadVideosLocal(context: Context, channelId: String, beginDate: DateTime, endDate: DateTime,
            onLoad: (videoList: List<Video>) -> Unit) = launch({
        val videoList = VideosInteractor.getInstance().getVideosLocalAsync(channelId, beginDate, endDate).await()
        onLoad(videoList)
    }, { e ->
        onLoad(emptyList())
        onError(context, e)
    })

    fun sortVideos(context: Context, videoList: ArrayList<Video>, sortMode: SortMode,
            sortDirection: Int, onSorted: () -> Unit) = launch({
        var maxViewCount = 0L
        if (sortMode == SortMode.RATING)
            videoList.forEach { maxViewCount = max(maxViewCount, it.viewCount ?: 0L) }

        videoList.sortWith(Comparator { o1, o2 ->
            var param1: Long? = null
            var param2: Long? = null
            when (sortMode) {
                SortMode.DATE -> {
                    param1 = o1.publishedAt.millis
                    param2 = o2.publishedAt.millis
                }
                SortMode.DURATION -> {
                    param1 = o1.duration?.toStandardDuration()?.millis
                    param2 = o2.duration?.toStandardDuration()?.millis
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
                SortMode.RATING -> {
                    param1 = calcRating(o1.likeCount ?: 0L, o1.dislikeCount ?: 0L, maxViewCount)
                    param2 = calcRating(o2.likeCount ?: 0L, o2.dislikeCount ?: 0L, maxViewCount)
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
    }, { e ->
        onSorted()
        onError(context, e)
    })

    private fun calcRating(likeCount: Long, dislikeCount: Long, maxViewCount: Long): Long {
        if (likeCount == 0L && dislikeCount == 0L)
            return 0L
        val count = likeCount + dislikeCount
        val z = 1.64485
        val x = if (likeCount >= dislikeCount) likeCount else dislikeCount
        val phat = x.toDouble() / count
        var rating = (phat + z * z / (2 * count) - z * sqrt((phat * (1 - phat) + z * z / (4 * count)) / count)) / (1 + z * z / count)
        rating *= count.toDouble() / maxViewCount
        return ((if (likeCount >= dislikeCount) rating else -rating) * 1000000000.0).toLong()
    }

}
