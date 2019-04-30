package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.GeneralStatisticsInteractor
import ru.sis.statube.interactor.StatisticsLastUpdatedInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.GeneralStatistics
import ru.sis.statube.model.GeneralStatisticsLastUpdated
import ru.sis.statube.model.Video
import ru.sis.statube.model.VideosStatisticsLastUpdated
import ru.sis.statube.presentation.Presenter

class StatisticsPresenter : Presenter() {

    fun loadGeneralStatisticsLastUpdatedDateTime(channelId: String, onLoad: (statisticsLastUpdated: GeneralStatisticsLastUpdated?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getGeneralStatisticsLastUpdatedAsync(channelId).await()
        onLoad(statisticsLastUpdated)
    }, {
        onLoad(null)
    })

    fun loadVideosStatisticsLastUpdatedDateTime(uploads: String, onLoad: (statisticsLastUpdated: VideosStatisticsLastUpdated?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getVideosStatisticsLastUpdatedAsync(uploads).await()
        onLoad(statisticsLastUpdated)
    }, {
        onLoad(null)
    })


    fun loadGeneralStatistics(context: Context, channelId: String, onLoad: (statistics: GeneralStatistics?) -> Unit,
                              onError: () -> Unit) = resolvedLaunch({
        val statistics = GeneralStatisticsInteractor.getInstance().getGeneralStatisticsAsync(context, channelId).await()
        onLoad(statistics)
    }, {
        onError()
    })

    fun loadGeneralStatisticsLocal(channelId: String, onLoad: (statistics: GeneralStatistics?) -> Unit) = resolvedLaunch({
        val statistics = GeneralStatisticsInteractor.getInstance().getGeneralStatisticsLocalAsync(channelId).await()
        onLoad(statistics)
    }, {
        onLoad(null)
    })


    fun loadVideosStatistics(context: Context, uploads: String, beginDate: DateTime, endDate: DateTime, channelId: String,
                             onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        showProgressDialog(context)
        val videoList = VideosInteractor.getInstance().getVideosAsync(context, uploads, beginDate, endDate, channelId).await()
        hideProgressDialog()
        onLoad(videoList)
    }, {
        hideProgressDialog()
        onLoad(null)
    })

    fun loadVideosStatisticsLocal(channelId: String, beginDate: DateTime, endDate: DateTime,
                                  onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        val videoList = VideosInteractor.getInstance().getVideosLocalAsync(channelId, beginDate, endDate).await()
        onLoad(videoList)
    }, {
        onLoad(null)
    })

}
