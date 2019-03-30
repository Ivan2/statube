package ru.sis.statube.presentation.screens.statistics

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import ru.sis.statube.R
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.SocialBladeInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.SocialBladeStatistics
import ru.sis.statube.model.Video

class StatisticsPresenter {

    private var dialog: MaterialDialog? = null

    fun loadStatistics(context: Context, channelId: String, onLoad: (videoList: List<Video>?) -> Unit) = resolvedLaunch({
        dialog = MaterialDialog(context).show {
            customView(R.layout.dialog_progress)
            cancelable(false)
        }
        val videoList = VideosInteractor.getInstance().searchAsync(context, channelId).await()
        dialog?.dismiss()
        onLoad(videoList)
    }, {
        dialog?.dismiss()
        onLoad(null)
    })

    fun loadSocialBladeStatistics(context: Context, channelId: String, onLoad: (statistics: SocialBladeStatistics?) -> Unit) = resolvedLaunch({
        val statistics = SocialBladeInteractor.getInstance().loadStatisticsAsync(context, channelId).await()
        onLoad(statistics)
    }, {
        dialog?.dismiss()
        onLoad(null)
    })

}
