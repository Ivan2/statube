package ru.sis.statube.model

import org.joda.time.DateTime
import java.io.Serializable

class Channel : Serializable {

    lateinit var id: String

    var publishedAt: DateTime? = null

    var title: String? = null

    var description: String? = null

    var thumbnails: Thumbnails? = null

    var country: String? = null

    var statistics: Statistics? = null

    var brandingSettings: BrandingSettings? = null

}