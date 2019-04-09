package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class VideosStatisticsLastUpdatedEntity : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var date: Long? = null

    open var beginDate: Long? = null

    open var endDate: Long? = null

}