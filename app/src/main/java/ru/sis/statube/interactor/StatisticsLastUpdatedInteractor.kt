package ru.sis.statube.interactor

import ru.sis.statube.additional.async
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

    fun setGeneralStatisticsLastUpdatedAsync(statisticsLastUpdated: GeneralStatisticsLastUpdated) = async {
        GeneralStatisticsLastUpdatedStore.getInstance().saveGeneralStatisticsLastUpdated(statisticsLastUpdated)
    }

    fun getGeneralStatisticsLastUpdatedAsync(id: String) = async {
        GeneralStatisticsLastUpdatedStore.getInstance().getGeneralStatisticsLastUpdated(id)
    }

    fun setVideosStatisticsLastUpdatedAsync(statisticsLastUpdated: VideosStatisticsLastUpdated) = async {
        VideosStatisticsLastUpdatedStore.getInstance().saveVideosStatisticsLastUpdated(statisticsLastUpdated)
    }

    fun getVideosStatisticsLastUpdatedAsync(id: String) = async {
        VideosStatisticsLastUpdatedStore.getInstance().getVideosStatisticsLastUpdated(id)
    }

}