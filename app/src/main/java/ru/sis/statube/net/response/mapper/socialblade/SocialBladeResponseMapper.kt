package ru.sis.statube.net.response.mapper.socialblade

import ru.sis.statube.model.SocialBladeStatistics
import ru.sis.statube.net.response.json.socialblade.SocialBladeResponse

class SocialBladeResponseMapper {

    fun map(from: SocialBladeResponse?): SocialBladeStatistics? {
        if (from == null)
            return null
        val statistics = SocialBladeStatistics()
        statistics.channelId = from.id?.channelId ?: return null
        statistics.uploads = from.data?.uploads
        statistics.subs = from.data?.subs
        statistics.views = from.data?.views
        statistics.avgDailySubs = from.data?.avgDailySubs
        statistics.avgDailyViews = from.data?.avgDailyViews
        statistics.subsByPeriod = SocialBladeSubsResponseMapper().map(from.charts?.subs)
        statistics.viewsByPeriod = SocialBladeViewsResponseMapper().map(from.charts?.views)
        statistics.growthSubs = from.charts?.growth?.subs
        statistics.growthViews = from.charts?.growth?.views
        statistics.dataDailyList = SocialBladeDataDailyResponseMapper().map(from.dataDailyArray)
        return statistics
    }

}
