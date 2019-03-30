package ru.sis.statube.model

import java.io.Serializable

class SocialBladeStatistics : Serializable {

    lateinit var channelId: String

    var uploads: Int? = null

    var subs: Int? = null

    var views: Int? = null

    var avgDailySubs: Int? = null

    var avgDailyViews: Int? = null

    var subsByPeriod: ValueByPeriod? = null

    var viewsByPeriod: ValueByPeriod? = null

    var growthSubs: Double? = null

    var growthViews: Double? = null

    var dataDailyList: List<SocialBladeDataDaily>? = null

}