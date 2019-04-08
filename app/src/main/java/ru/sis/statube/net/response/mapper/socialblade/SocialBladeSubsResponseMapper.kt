package ru.sis.statube.net.response.mapper.socialblade

import ru.sis.statube.model.ValueByPeriod
import ru.sis.statube.net.response.json.socialblade.SocialBladeSubsResponse

class SocialBladeSubsResponseMapper {

    fun map(from: SocialBladeSubsResponse?): ValueByPeriod? {
        if (from == null)
            return null
        val subs = ValueByPeriod()
        subs.by14 = from.subs14?.toLongOrNull()
        subs.by30 = from.subs30?.toLongOrNull()
        subs.by60 = from.subs60?.toLongOrNull()
        subs.by90 = from.subs90?.toLongOrNull()
        subs.by180 = from.subs180?.toLongOrNull()
        subs.by365 = from.subs365?.toLongOrNull()
        return subs
    }

}