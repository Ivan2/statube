package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.SocialBladeInteractor
import ru.sis.statube.interactor.StatisticsLastUpdatedInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.SocialBladeStatistics
import ru.sis.statube.model.StatisticsLastUpdated
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.Presenter

class StatisticsPresenter : Presenter() {

    fun setGeneralStatisticsLastUpdatedDateTime(channelId: String, dateTime: DateTime?) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdated().apply {
            this.id = "Channel $channelId"
            this.date = dateTime
        }
        StatisticsLastUpdatedInteractor.getInstance().setStatisticsLastUpdatedAsync(statisticsLastUpdated).await()
    }, {})

    fun loadGeneralStatisticsLastUpdatedDateTime(channelId: String, onLoad: (date: DateTime?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getStatisticsLastUpdatedAsync("Channel $channelId").await()
        onLoad(statisticsLastUpdated?.date)
    }, {
        onLoad(null)
    })

    fun setVideosStatisticsLastUpdatedDateTime(uploads: String, dateTime: DateTime?) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdated().apply {
            this.id = "Uploads $uploads"
            this.date = dateTime
        }
        StatisticsLastUpdatedInteractor.getInstance().setStatisticsLastUpdatedAsync(statisticsLastUpdated).await()
    }, {})

    fun loadVideosStatisticsLastUpdatedDateTime(uploads: String, onLoad: (date: DateTime?) -> Unit) = resolvedLaunch({
        val statisticsLastUpdated = StatisticsLastUpdatedInteractor.getInstance().getStatisticsLastUpdatedAsync("Uploads $uploads").await()
        onLoad(statisticsLastUpdated?.date)
    }, {
        onLoad(null)
    })


    fun loadSocialBladeStatistics(context: Context, channelId: String, onLoad: (statistics: SocialBladeStatistics?) -> Unit,
                                  onError: () -> Unit) = resolvedLaunch({
        val statistics = SocialBladeInteractor.getInstance().loadStatisticsAsync(context, channelId).await()
        onLoad(statistics)
    }, {
        onError()
    })

    fun loadSocialBladeLocalStatistics(channelId: String, onLoad: (statistics: SocialBladeStatistics?) -> Unit) = resolvedLaunch({
        val statistics = SocialBladeInteractor.getInstance().loadLocalStatisticsAsync(channelId).await()
        onLoad(statistics)
    }, {
        onLoad(null)
    })


    fun loadStatistics(context: Context, uploads: String, beginDate: DateTime, endDate: DateTime,
                       onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        showProgressDialog(context)
        val videoList = VideosInteractor.getInstance().loadAsync(context, uploads, beginDate, endDate).await()
        hideProgressDialog()
        onLoad(videoList)
    }, {
        hideProgressDialog()
        onLoad(null)
    })

    fun loadLocalStatistics(uploads: String, beginDate: DateTime, endDate: DateTime,
                            onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        val videoList = VideosInteractor.getInstance().loadLocalVideosAsync(uploads).await()
        onLoad(videoList)
    }, {
        onLoad(null)
    })

}
