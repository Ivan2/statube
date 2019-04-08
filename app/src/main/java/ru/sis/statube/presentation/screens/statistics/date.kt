package ru.sis.statube.presentation.screens.statistics

import org.joda.time.*

fun DateTime.formatUpdate(): String {
    return this.toString("dd MMM HH:mm")
}

fun DateTime.formatPeriod(): String {
    return this.toString("dd.MM.yyyy")
}

fun DateTime.formatChartDate(): String {
    return this.toString("dd MMM")
}