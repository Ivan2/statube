package ru.sis.statube.interactor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.db.store.StatisticsLastUpdatedStore
import ru.sis.statube.model.StatisticsLastUpdated

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

    fun setStatisticsLastUpdatedAsync(statisticsLastUpdated: StatisticsLastUpdated) = GlobalScope.async {
        StatisticsLastUpdatedStore.getInstance().saveStatisticsLastUpdated(statisticsLastUpdated)
    }

    fun getStatisticsLastUpdatedAsync(id: String) = GlobalScope.async {
        StatisticsLastUpdatedStore.getInstance().getStatisticsLastUpdated(id)
    }

}