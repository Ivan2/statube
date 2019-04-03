package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.SocialBladeInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.SocialBladeStatistics
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.Presenter

class StatisticsPresenter : Presenter() {

    fun loadSocialBladeStatistics(context: Context, channelId: String, onLoad: (statistics: SocialBladeStatistics?) -> Unit) = resolvedLaunch({
        val statistics = SocialBladeInteractor.getInstance().loadStatisticsAsync(context, channelId).await()
        onLoad(statistics)
    }, {
        onLoad(null)
    })

    fun loadStatistics(context: Context, channelId: String, onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        showProgressDialog(context)
        val videoList = VideosInteractor.getInstance().searchAsync(context, channelId).await()
        hideProgressDialog()
        onLoad(videoList)
    }, {
        hideProgressDialog()
        onLoad(null)
    })

}
