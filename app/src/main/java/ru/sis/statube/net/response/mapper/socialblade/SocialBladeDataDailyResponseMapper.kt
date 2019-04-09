package ru.sis.statube.net.response.mapper.socialblade

import ru.sis.statube.additional.parseSocialBladeDate
import ru.sis.statube.model.DataDaily
import ru.sis.statube.net.response.json.socialblade.SocialBladeDataDailyResponse

class SocialBladeDataDailyResponseMapper {

    fun map(from: SocialBladeDataDailyResponse?): DataDaily? {
        if (from == null)
            return null
        val dataDaily = DataDaily()
        dataDaily.date = from.date?.parseSocialBladeDate() ?: return null
        dataDaily.subs = from.subs?.toLongOrNull() ?: return null
        dataDaily.views = from.views?.toLongOrNull() ?: return null
        return dataDaily
    }

    fun map(from: Array<SocialBladeDataDailyResponse>?): List<DataDaily>? {
        if (from == null)
            return null
        val dataDailyList = ArrayList<DataDaily>(from.size)
        from.forEach { dataDailyResponse ->
            map(dataDailyResponse)?.let { dataDaily ->
                dataDailyList.add(dataDaily)
            }
        }
        return dataDailyList
    }

}