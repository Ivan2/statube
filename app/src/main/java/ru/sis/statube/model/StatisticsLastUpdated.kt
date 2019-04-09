package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class StatisticsLastUpdated : Serializable {

    lateinit var id: String

    var date: DateTime? = null

}