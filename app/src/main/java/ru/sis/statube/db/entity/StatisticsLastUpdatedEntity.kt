package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class StatisticsLastUpdatedEntity : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var date: Long? = null

}