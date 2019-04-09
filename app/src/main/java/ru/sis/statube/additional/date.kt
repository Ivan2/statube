package ru.sis.statube.additional

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

private const val YOUTUBE_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
private const val SOCIAL_BLADE_DATE_PATTERN = "yyyy-MM-dd"

fun String.parseYoutubeDateTime(): DateTime? {
    val formatter = DateTimeFormat.forPattern(YOUTUBE_DATE_TIME_PATTERN)
    return try {
        formatter.parseDateTime(this)
    } catch (e: Exception) {
        null
    }
}

fun String.parseSocialBladeDate(): DateTime? {
    val formatter = DateTimeFormat.forPattern(SOCIAL_BLADE_DATE_PATTERN)
    return try {
        formatter.parseDateTime(this)
    } catch (e: Exception) {
        null
    }
}

fun DateTime.date(): DateTime {
    return this.withTime(0, 0, 0, 0)
}