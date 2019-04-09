package ru.sis.statube.interactor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.db.store.GeneralStatisticsLastUpdatedStore
import ru.sis.statube.db.store.VideosStatisticsLastUpdatedStore
import ru.sis.statube.model.GeneralStatisticsLastUpdated
import ru.sis.statube.model.VideosStatisticsLastUpdated

class StatisticsLastUpdatedInteractor : Interactor() {

    companion object {
        private var INSTANCE: StatisticsLastUpdatedInteractor? = null
        fun getInstance(): StatisticsLastUpdatedInteractor {
            val instance = INSTANCE ?: StatisticsLastUpdatedInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    fun setGeneralStatisticsLastUpdatedAsync(statisticsLastUpdated: GeneralStatisticsLastUpdated) = GlobalScope.async {
        GeneralStatisticsLastUpdatedStore.getInstance().saveGeneralStatisticsLastUpdated(statisticsLastUpdated)
    }

    fun getGeneralStatisticsLastUpdatedAsync(id: String) = GlobalScope.async {
        GeneralStatisticsLastUpdatedStore.getInstance().getGeneralStatisticsLastUpdated(id)
    }

    fun setVideosStatisticsLastUpdatedAsync(statisticsLastUpdated: VideosStatisticsLastUpdated) = GlobalScope.async {
        VideosStatisticsLastUpdatedStore.getInstance().saveVideosStatisticsLastUpdated(statisticsLastUpdated)
    }

    fun getVideosStatisticsLastUpdatedAsync(id: String) = GlobalScope.async {
        VideosStatisticsLastUpdatedStore.getInstance().getVideosStatisticsLastUpdated(id)
    }

}