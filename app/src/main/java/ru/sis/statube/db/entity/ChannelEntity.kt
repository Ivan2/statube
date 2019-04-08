package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class ChannelEntity : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var title: String? = null

    open var description: String? = null

    open var thumbnail: String? = null

}