package ru.sis.statube.model

import java.io.Serializable

class GeneralStatistics : Serializable {

    lateinit var channelId: String

    var avgDailySubs: Long? = null

    var avgDailyViews: Long? = null

    var subsByPeriod: ValueByPeriod? = null

    var viewsByPeriod: ValueByPeriod? = null

    var growthSubs: String? = null

    var growthViews: String? = null

    var dataDailyList: List<DataDaily> = emptyList()

}