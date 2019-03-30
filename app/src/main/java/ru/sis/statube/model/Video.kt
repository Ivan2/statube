package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class Video : Serializable {

    lateinit var id: String

    lateinit var publishedAt: DateTime

    var title: String? = null

    var viewCount: Int? = null

    var likeCount: Int? = null

    var dislikeCount: Int? = null

    var commentCount: Int? = null

}