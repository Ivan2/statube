package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class ValueByPeriodEntity : RealmObject() {

    open var by14: Int? = null

    open var by30: Int? = null

    open var by60: Int? = null

    open var by90: Int? = null

    open var by180: Int? = null

    open var by365: Int? = null

}