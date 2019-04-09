package ru.sis.statube.db.mapper

import ru.sis.statube.db.entity.GeneralStatisticsEntity
import ru.sis.statube.model.GeneralStatistics

class GeneralStatisticsEntityMapper {

    fun map(from: GeneralStatisticsEntity?): GeneralStatistics? {
        if (from == null)
            return null
        val statistics = GeneralStatistics()
        statistics.channelId = from.channelId
        statistics.avgDailySubs = from.avgDailySubs
        statistics.avgDailyViews = from.avgDailyViews
        statistics.subsByPeriod = ValueByPeriodEntityMapper().map(from.subsByPeriod)
        statistics.viewsByPeriod = ValueByPeriodEntityMapper().map(from.viewsByPeriod)
        statistics.growthSubs = from.growthSubs
        statistics.growthViews = from.growthViews
        statistics.dataDailyList = DataDailyEntityMapper().map(from.dataDailyList)
        return statistics
    }

    fun reverseMap(from: GeneralStatistics): GeneralStatisticsEntity {
        val statistics = GeneralStatisticsEntity()
        statistics.channelId = from.channelId
        statistics.avgDailySubs = from.avgDailySubs
        statistics.avgDailyViews = from.avgDailyViews
        statistics.subsByPeriod = ValueByPeriodEntityMapper().reverseMap(from.subsByPeriod)
        statistics.viewsByPeriod = ValueByPeriodEntityMapper().reverseMap(from.viewsByPeriod)
        statistics.growthSubs = from.growthSubs
        statistics.growthViews = from.growthViews
        statistics.dataDailyList = DataDailyEntityMapper().reverseMap(from.dataDailyList)
        return statistics
    }

}