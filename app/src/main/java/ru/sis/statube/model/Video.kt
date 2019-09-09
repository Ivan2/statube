package ru.sis.statube.model

import org.joda.time.DateTime
import org.joda.time.Period
import java.io.Serializable

class Video : Serializable {

    lateinit var id: String

    lateinit var uploads: String

    lateinit var publishedAt: DateTime

    lateinit var channelId: String

    var title: String? = null

    var description: String? = null

    var thumbnail: String? = null

    var tags: Array<String>? = null

    var duration: Period? = null

    var viewCount: Long? = null

    var likeCount: Long? = null

    var dislikeCount: Long? = null

    var commentCount: Long? = null

    var scheduledStartTime: DateTime? = null

    var actualStartTime: DateTime? = null

    var actualEndTime: DateTime? = null

}