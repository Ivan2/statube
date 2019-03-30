package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class SocialBladeDataDaily : Serializable {

    lateinit var date: DateTime

    var subs: Int = 0

    var views: Int = 0

}