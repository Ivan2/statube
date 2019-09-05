package ru.sis.statube.additional

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISOPeriodFormat

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

fun DateTime.formatYoutubeDateTime(): String {
    return this.toString(YOUTUBE_DATE_TIME_PATTERN)
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

fun DateTime.formatPeriod(): String {
    return this.toString("dd.MM.yyyy")
}

fun DateTime.formatUpdate(): String {
    return this.toString("dd MMM HH:mm")
}

fun String.parseYoutubePeriod(): Period? {
    return ISOPeriodFormat.standard().parsePeriod(this)
}

fun Period.formatDuration(): String {
    return String.format("%d:%02d:%02d", this.hours, this.minutes, this.seconds)
}
