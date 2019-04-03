package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class Channel : Serializable {

    lateinit var id: String

    var publishedAt: DateTime? = null

    var title: String? = null

    var description: String? = null

    var thumbnail: String? = null

    var country: String? = null

    var viewCount: String? = null

    var subscriberCount: String? = null

    var videoCount: String? = null

    var keywords: String? = null

    var bannerImageUrl: String? = null

    var isFavourite: Boolean = false

}