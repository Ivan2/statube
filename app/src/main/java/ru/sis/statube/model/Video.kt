package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class Video : Serializable {

    lateinit var id: String

    lateinit var uploads: String

    lateinit var publishedAt: DateTime

    var viewCount: Long? = null

    var likeCount: Long? = null

    var dislikeCount: Long? = null

    var commentCount: Long? = null

}