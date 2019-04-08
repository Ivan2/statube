package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class DataDailyEntity : RealmObject() {

    open var date: Long = 0

    open var subs: Long? = null

    open var views: Long? = null

}