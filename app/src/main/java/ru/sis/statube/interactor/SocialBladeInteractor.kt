package ru.sis.statube.interactor

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.additional.SOCIAL_BLADE_API_URL
import ru.sis.statube.db.store.SocialBladeStatisticsStore
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.socialblade.SocialBladeResponse
import ru.sis.statube.net.response.mapper.socialblade.SocialBladeResponseMapper

class SocialBladeInteractor : Interactor() {

    companion object {
        private var INSTANCE: SocialBladeInteractor? = null
        fun getInstance(): SocialBladeInteractor {
            val instance = INSTANCE ?: SocialBladeInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val statisticsPath = "statistics?query=statistics&username=%s&email=%s&token=%s"

    fun loadStatisticsAsync(context: Context, channelId: String) = GlobalScope.async {
        val config = loadConfig(context)
        val url = "$SOCIAL_BLADE_API_URL${String.format(statisticsPath, channelId,
            config.socialBladeEmail, config.socialBladeToken)}"
        val response = OkRequest.getInstance().get(url)
        val socialBladeResponse = Gson().fromJson(response, SocialBladeResponse::class.java)
        val statistics = SocialBladeResponseMapper().map(socialBladeResponse)
        statistics?.let {
            SocialBladeStatisticsStore.getInstance().saveSocialBladeStatistics(channelId, statistics)
        }
        statistics
    }

    fun loadLocalStatisticsAsync(channelId: String) = GlobalScope.async {
        SocialBladeStatisticsStore.getInstance().getSocialBladeStatistics(channelId)
    }

}