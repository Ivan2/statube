package ru.sis.statube.additional

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

private const val ISO_DATE_PATTERN = "YYYY-MM-dd'T'HH:mm:ssZZ"
private const val ISO_DAY_PATTERN = "YYYY-MM-dd"

private const val PROGRAM_DAY_DATE_PATTERN = "dd.MM.YYYY"
private const val TELECAST_TIME_PATTERN = "HH:mm"
private const val PROGRAM_DAY_DATE_TAB_PATTERN = "EE, dd.MM"

val SERVER_TIME_ZONE: DateTimeZone = DateTimeZone.forID("Europe/Moscow")

fun DateTime.formatISODate(): String = this.toString(ISO_DATE_PATTERN)

fun String.parseISODate(): DateTime? {
    val formatter = DateTimeFormat.forPattern(ISO_DATE_PATTERN)
    return try {
        formatter.parseDateTime(this)
    } catch (e: Exception) {
        null
    }
}

fun String.parseISODay(): DateTime? {
    val formatter = DateTimeFormat.forPattern(ISO_DAY_PATTERN)
    return try {
        formatter.parseDateTime(this)
    } catch (e: Exception) {
        null
    }
}

fun DateTime.formatProgramDayDateTab(): String = this.toString(PROGRAM_DAY_DATE_TAB_PATTERN)

fun DateTime.formatTelecastTime(): String = this.toString(TELECAST_TIME_PATTERN)


fun DateTime.date(): DateTime = this.withTime(0, 0, 0, 0)

fun DateTime.isToday(): Boolean {
    return this.toLocalDate() == LocalDate.now()
}

fun DateTime.isTomorrow(): Boolean {
    return this.toLocalDate() == LocalDate.now().plusDays(1)
}

fun DateTime.betweenNow(): String {
    val seconds = Seconds.secondsBetween(DateTime.now(SERVER_TIME_ZONE), this.withZoneRetainFields(SERVER_TIME_ZONE)).seconds
    return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60)
}