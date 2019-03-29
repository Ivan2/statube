package ru.sis.statube.model

import org.joda.time.DateTime

class Channel {

    lateinit var id: String

    var publishedAt: DateTime? = null

    var title: String? = null

    var description: String? = null

    var thumbnails: Thumbnails? = null

}