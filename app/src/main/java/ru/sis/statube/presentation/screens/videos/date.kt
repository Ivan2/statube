package ru.sis.statube.presentation.screens.videos

import org.joda.time.DateTime

fun DateTime.formatUpdate(): String {
    return this.toString("dd MMM HH:mm")
}

fun DateTime.formatPeriod(): String {
    return this.toString("dd.MM.yyyy")
}