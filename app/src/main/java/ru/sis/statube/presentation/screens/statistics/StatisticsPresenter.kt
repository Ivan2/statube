package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.additional.launch
import ru.sis.statube.interactor.GeneralStatisticsInteractor
import ru.sis.statube.interactor.StatisticsLastUpdatedInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.GeneralStatistics
import ru.sis.statube.model.GeneralStatisticsLastUpdated
import ru.sis.statube.model.Video
import ru.sis.statube.model.VideosStatisticsLastUpdated
import ru.sis.statube.presentation.presenter.FullVideoLoadingPresenter

class StatisticsPresenter : FullVideoLoadingPresenter() {

    fun loadGeneralStatisticsLastUpdatedDateTime(context: Context, channelId: String,
            onLoad: (statisticsLastUpdated: GeneralStatisticsLastUpdated?) -> Unit) = launch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getGeneralStatisticsLastUpdatedAsync(channelId).await()
        onLoad(statisticsLastUpdated)
    }, { e ->
        onLoad(null)
        onError(context, e)
    })

    fun loadVideosStatisticsLastUpdatedDateTime(context: Context, uploads: String,
            onLoad: (statisticsLastUpdated: VideosStatisticsLastUpdated?) -> Unit) = launch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getVideosStatisticsLastUpdatedAsync(uploads).await()
        onLoad(statisticsLastUpdated)
    }, { e ->
        onLoad(null)
        onError(context, e)
    })


    fun loadGeneralStatistics(context: Context, channelId: String,
            onLoad: (statistics: GeneralStatistics?) -> Unit, onError: () -> Unit) = launch({
        val statistics = GeneralStatisticsInteractor.getInstance().getGeneralStatisticsAsync(context, channelId).await()
        onLoad(statistics)
    }, { e ->
        onError()
        onError(context, e)
    })

    fun loadGeneralStatisticsLocal(context: Context, channelId: String,
            onLoad: (statistics: GeneralStatistics?) -> Unit) = launch({
        val statistics = GeneralStatisticsInteractor.getInstance().getGeneralStatisticsLocalAsync(channelId).await()
        onLoad(statistics)
    }, { e ->
        onLoad(null)
        onError(context, e)
    })


    fun loadVideosStatistics(context: Context, uploads: String, beginDate: DateTime,
            endDate: DateTime, channelId: String, onLoad: (videoList: List<Video>) -> Unit) = launch({
        val videoList = VideosInteractor.getInstance().getVideosAsync(context, uploads, beginDate, endDate, channelId).await()
        onLoad(videoList)
    }, { e ->
        onLoad(emptyList())
        onError(context, e)
    })

    fun loadVideosStatisticsLocal(context: Context, channelId: String, beginDate: DateTime,
            endDate: DateTime, onLoad: (videoList: List<Video>) -> Unit) = launch({
        val videoList = VideosInteractor.getInstance().getVideosLocalAsync(channelId, beginDate, endDate).await()
        onLoad(videoList)
    }, { e ->
        onLoad(emptyList())
        onError(context, e)
    })

}
