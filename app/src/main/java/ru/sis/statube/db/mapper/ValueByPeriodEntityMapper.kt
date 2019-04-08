package ru.sis.statube.db.mapper

import ru.sis.statube.db.entity.ValueByPeriodEntity
import ru.sis.statube.model.ValueByPeriod

class ValueByPeriodEntityMapper {

    fun map(from: ValueByPeriodEntity?): ValueByPeriod? {
        if (from == null)
            return null
        val valueByPeriod = ValueByPeriod()
        valueByPeriod.by14 = from.by14
        valueByPeriod.by30 = from.by30
        valueByPeriod.by60 = from.by60
        valueByPeriod.by90 = from.by90
        valueByPeriod.by180 = from.by180
        valueByPeriod.by365 = from.by365
        return valueByPeriod
    }

    fun reverseMap(from: ValueByPeriod?): ValueByPeriodEntity? {
        if (from == null)
            return null
        val valueByPeriod = ValueByPeriodEntity()
        valueByPeriod.by14 = from.by14
        valueByPeriod.by30 = from.by30
        valueByPeriod.by60 = from.by60
        valueByPeriod.by90 = from.by90
        valueByPeriod.by180 = from.by180
        valueByPeriod.by365 = from.by365
        return valueByPeriod
    }

}