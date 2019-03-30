package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.model.Thumbnail
import ru.sis.statube.net.response.json.youtube.ThumbnailResponse

class ThumbnailResponseMapper {

    fun map(from: ThumbnailResponse?): Thumbnail? {
        if (from == null)
            return null
        val thumbnail = Thumbnail()
        thumbnail.url = from.url ?: return null
        return thumbnail
    }

}