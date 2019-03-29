package ru.sis.statube.net.response.mapper

import ru.sis.statube.model.Thumbnail
import ru.sis.statube.net.response.json.ThumbnailResponse

class ThumbnailResponseMapper {

    fun map(from: ThumbnailResponse?): Thumbnail? {
        if (from == null)
            return null
        val thumbnail = Thumbnail()
        thumbnail.url = from.url ?: return null
        return thumbnail
    }

}