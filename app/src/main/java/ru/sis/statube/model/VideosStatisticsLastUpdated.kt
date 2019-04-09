package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class VideosStatisticsLastUpdated : Serializable {

    lateinit var id: String

    lateinit var date: DateTime

    lateinit var beginDate: DateTime

    lateinit var endDate: DateTime

}