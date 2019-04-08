package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import org.joda.time.DateTime
import ru.sis.statube.Preferences
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.SocialBladeInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.SocialBladeDataDaily
import ru.sis.statube.model.SocialBladeStatistics
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.Presenter

class StatisticsPresenter : Presenter() {

    fun setStatistics1LastUpdatedDateTime(context: Context, dateTime: DateTime?) =
        Preferences.getInstance().setStatistics1LastUpdatedDateTime(context, dateTime)

    fun getStatistics1LastUpdatedDateTime(context: Context): DateTime? =
        Preferences.getInstance().getStatistics1LastUpdatedDateTime(context)

    fun setStatistics2LastUpdatedDateTime(context: Context, dateTime: DateTime?) =
        Preferences.getInstance().setStatistics2LastUpdatedDateTime(context, dateTime)

    fun getStatistics2LastUpdatedDateTime(context: Context): DateTime? =
        Preferences.getInstance().getStatistics2LastUpdatedDateTime(context)


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
