package ru.sis.statube.net.response.mapper.socialblade

import ru.sis.statube.additional.parseSocialBladeDate
import ru.sis.statube.model.SocialBladeDataDaily
import ru.sis.statube.net.response.json.socialblade.SocialBladeDataDailyResponse

class SocialBladeDataDailyResponseMapper {

    fun map(from: SocialBladeDataDailyResponse?): SocialBladeDataDaily? {
        if (from == null)
            return null
        val dataDaily = SocialBladeDataDaily()
        dataDaily.date = from.date?.parseSocialBladeDate() ?: return null
        dataDaily.subs = from.subs ?: return null
        dataDaily.views = from.views ?: return null
        return dataDaily
    }

    fun map(from: Array<SocialBladeDataDailyResponse>?): List<SocialBladeDataDaily>? {
        if (from == null)
            return null
        val dataDailyList = ArrayList<SocialBladeDataDaily>(from.size)
        from.forEach { dataDailyResponse ->
            map(dataDailyResponse)?.let { dataDaily ->
                dataDailyList.add(dataDaily)
            }
        }
        return dataDailyList
    }

}