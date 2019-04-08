package ru.sis.statube.db.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class SocialBladeStatisticsEntity : RealmObject() {

    @PrimaryKey
    open var channelId: String = ""

    open var avgDailySubs: Int? = null

    open var avgDailyViews: Int? = null

    open var subsByPeriod: ValueByPeriodEntity? = null

    open var viewsByPeriod: ValueByPeriodEntity? = null

    open var growthSubs: Double? = null

    open var growthViews: Double? = null

    open var dataDailyList = RealmList<DataDailyEntity>()

}
