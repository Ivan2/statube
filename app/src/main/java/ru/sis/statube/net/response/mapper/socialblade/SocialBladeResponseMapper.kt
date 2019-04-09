package ru.sis.statube.net.response.mapper.socialblade

import ru.sis.statube.model.GeneralStatistics
import ru.sis.statube.net.response.json.socialblade.SocialBladeResponse

class SocialBladeResponseMapper {

    fun map(from: SocialBladeResponse?): GeneralStatistics? {
        if (from == null)
            return null
        val statistics = GeneralStatistics()
        statistics.channelId = from.id?.channelId ?: return null
        statistics.avgDailySubs = from.data?.avgDailySubs?.toLongOrNull()
        statistics.avgDailyViews = from.data?.avgDailyViews?.toLongOrNull()
        statistics.subsByPeriod = SocialBladeSubsResponseMapper().map(from.charts?.subs)
        statistics.viewsByPeriod = SocialBladeViewsResponseMapper().map(from.charts?.views)
        statistics.growthSubs = from.charts?.growth?.subs
        statistics.growthViews = from.charts?.growth?.views
        statistics.dataDailyList = SocialBladeDataDailyResponseMapper().map(from.dataDailyArray) ?: emptyList()
        return statistics
    }

}
