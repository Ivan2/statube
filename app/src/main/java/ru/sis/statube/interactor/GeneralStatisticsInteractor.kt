package ru.sis.statube.interactor

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.joda.time.DateTime
import ru.sis.statube.additional.SOCIAL_BLADE_API_URL
import ru.sis.statube.db.store.GeneralStatisticsStore
import ru.sis.statube.model.GeneralStatisticsLastUpdated
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.socialblade.SocialBladeResponse
import ru.sis.statube.net.response.mapper.socialblade.SocialBladeResponseMapper

class GeneralStatisticsInteractor : Interactor() {

    companion object {
        private var INSTANCE: GeneralStatisticsInteractor? = null
        fun getInstance(): GeneralStatisticsInteractor {
            val instance = INSTANCE ?: GeneralStatisticsInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val statisticsPath = "statistics?query=statistics&username=%s&email=%s&token=%s"

    fun getGeneralStatisticsAsync(context: Context, channelId: String) = GlobalScope.async {
        val config = getConfig(context)
        val url = "$SOCIAL_BLADE_API_URL${String.format(statisticsPath, channelId,
            config.socialBladeEmail, config.socialBladeToken)}"
        val response = OkRequest.getInstance().get(url)
        val socialBladeResponse = Gson().fromJson(response, SocialBladeResponse::class.java)
        val statistics = SocialBladeResponseMapper().map(socialBladeResponse)
        statistics?.let {
            GeneralStatisticsStore.getInstance().saveGeneralStatistics(statistics)
        }
        val statisticsLastUpdated = GeneralStatisticsLastUpdated().apply {
            this.id = channelId
            this.date = DateTime.now()
        }
        StatisticsLastUpdatedInteractor.getInstance().setGeneralStatisticsLastUpdatedAsync(statisticsLastUpdated).await()
        statistics
    }

    fun getGeneralStatisticsLocalAsync(channelId: String) = GlobalScope.async {
        GeneralStatisticsStore.getInstance().getGeneralStatistics(channelId)
    }

}