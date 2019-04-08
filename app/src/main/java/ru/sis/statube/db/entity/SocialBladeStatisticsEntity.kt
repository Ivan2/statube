package ru.sis.statube.db.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class SocialBladeStatisticsEntity : RealmObject() {

    @PrimaryKey
    open var channelId: String = ""

    open var avgDailySubs: Long? = null

    open var avgDailyViews: Long? = null

    open var subsByPeriod: ValueByPeriodEntity? = null

    open var viewsByPeriod: ValueByPeriodEntity? = null

    open var growthSubs: String? = null

    open var growthViews: String? = null

    open var dataDailyList = RealmList<DataDailyEntity>()

}
