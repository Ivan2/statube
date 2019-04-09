package ru.sis.statube.presentation.screens.channel

import org.joda.time.DateTime

fun DateTime.formatPublishedAt(): String {
    return this.toString("dd.MM.yyyy")
}