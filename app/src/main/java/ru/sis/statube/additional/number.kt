package ru.sis.statube.additional

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

private val decimalFormatter = (NumberFormat.getNumberInstance(Locale.ENGLISH) as DecimalFormat).apply {
    this.applyPattern("###,###.#")
}

fun Float.format(): String {
    return decimalFormatter.format(this)
}

fun Long.format(): String {
    return decimalFormatter.format(this)
}
