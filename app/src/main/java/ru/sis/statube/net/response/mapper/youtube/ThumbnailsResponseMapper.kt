package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.model.Thumbnails
import ru.sis.statube.net.response.json.youtube.ThumbnailsResponse

class ThumbnailsResponseMapper {

    fun map(from: ThumbnailsResponse?): Thumbnails? {
        if (from == null)
            return null
        val thumbnails = Thumbnails()
        val thumbnailResponseMapper = ThumbnailResponseMapper()
        thumbnails.default = thumbnailResponseMapper.map(from.default)
        thumbnails.medium = thumbnailResponseMapper.map(from.medium)
        thumbnails.high = thumbnailResponseMapper.map(from.high)
        return thumbnails
    }

}