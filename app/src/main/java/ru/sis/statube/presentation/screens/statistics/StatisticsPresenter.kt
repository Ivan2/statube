package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.GeneralStatisticsInteractor
import ru.sis.statube.interactor.StatisticsLastUpdatedInteractor
import ru.sis.statube.interactor.VideosStatisticsInteractor
import ru.sis.statube.model.GeneralStatistics
import ru.sis.statube.model.StatisticsLastUpdated
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.Presenter

class StatisticsPresenter : Presenter() {

    private val channelIdTemplate = "Channel %s"
    private val uploadsTemplate = "Uploads %s"

    fun setGeneralStatisticsLastUpdatedDateTime(channelId: String, dateTime: DateTime?) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdated().apply {
            this.id = String.format(channelIdTemplate, channelId)
            this.date = dateTime
        }
        StatisticsLastUpdatedInteractor.getInstance().setStatisticsLastUpdatedAsync(statisticsLastUpdated).await()
    }, {})

    fun loadGeneralStatisticsLastUpdatedDateTime(channelId: String, onLoad: (date: DateTime?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getStatisticsLastUpdatedAsync(
            String.format(channelIdTemplate, channelId)).await()
        onLoad(statisticsLastUpdated?.date)
    }, {
        onLoad(null)
    })

    fun setVideosStatisticsLastUpdatedDateTime(uploads: String, dateTime: DateTime?) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdated().apply {
            this.id = String.format(uploadsTemplate, uploads)
            this.date = dateTime
        }
        StatisticsLastUpdatedInteractor.getInstance().setStatisticsLastUpdatedAsync(statisticsLastUpdated).await()
    }, {})

    fun loadVideosStatisticsLastUpdatedDateTime(uploads: String, onLoad: (date: DateTime?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getStatisticsLastUpdatedAsync(
            String.format(uploadsTemplate, uploads)).await()
        onLoad(statisticsLastUpdated?.date)
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


    fun loadVideosStatistics(context: Context, uploads: String, beginDate: DateTime, endDate: DateTime,
                             onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        showProgressDialog(context)
        val videoList = VideosStatisticsInteractor.getInstance().getVideosStatisticsAsync(context, uploads, beginDate, endDate).await()
        hideProgressDialog()
        onLoad(videoList)
    }, {
        hideProgressDialog()
        onLoad(null)
    })

    fun loadVideosStatisticsLocal(uploads: String, beginDate: DateTime, endDate: DateTime,
                                  onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        val videoList = VideosStatisticsInteractor.getInstance().getVideosStatisticsLocalAsync(uploads).await()
        onLoad(videoList)
    }, {
        onLoad(null)
    })

}
