package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class DataDaily : Serializable {

    lateinit var date: DateTime

    var subs: Long = 0

    var views: Long = 0

}