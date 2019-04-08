package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class ValueByPeriodEntity : RealmObject() {

    open var by14: Long? = null

    open var by30: Long? = null

    open var by60: Long? = null

    open var by90: Long? = null

    open var by180: Long? = null

    open var by365: Long? = null

}