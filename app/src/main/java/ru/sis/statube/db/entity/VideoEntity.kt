package ru.sis.statube.db.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class VideoEntity : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var channelId: String? = null

    open var publishedAt: Long? = null

    open var title: String? = null

    open var description: String? = null

    open var thumbnail: String? = null

    open var viewCount: Long? = null

    open var likeCount: Long? = null

    open var dislikeCount: Long? = null

    open var commentCount: Long? = null

}