package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class PlayListItem : Serializable {

    lateinit var id: String

    lateinit var videoId: String

    lateinit var videoPublishedAt: DateTime

}