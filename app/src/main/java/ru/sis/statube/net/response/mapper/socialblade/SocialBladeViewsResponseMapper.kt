package ru.sis.statube.net.response.mapper.socialblade

import ru.sis.statube.model.ValueByPeriod
import ru.sis.statube.net.response.json.socialblade.SocialBladeViewsResponse

class SocialBladeViewsResponseMapper {

    fun map(from: SocialBladeViewsResponse?): ValueByPeriod? {
        if (from == null)
            return null
        val views = ValueByPeriod()
        views.by14 = from.views14?.toLongOrNull()
        views.by30 = from.views30?.toLongOrNull()
        views.by60 = from.views60?.toLongOrNull()
        views.by90 = from.views90?.toLongOrNull()
        views.by180 = from.views180?.toLongOrNull()
        views.by365 = from.views365?.toLongOrNull()
        return views
    }

}