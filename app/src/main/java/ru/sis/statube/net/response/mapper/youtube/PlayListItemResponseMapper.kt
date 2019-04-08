package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.additional.parseYoutubeDateTime
import ru.sis.statube.model.PlayListItem
import ru.sis.statube.net.response.json.youtube.playListItem.PlayListItemResponse

class PlayListItemResponseMapper {

    fun map(from: PlayListItemResponse?): PlayListItem? {
        if (from == null || from.kind != "youtube#playlistItem")
            return null
        val playListItem = PlayListItem()
        playListItem.id = from.id ?: return null
        playListItem.videoId = from.contentDetails?.videoId ?: return null
        playListItem.videoPublishedAt = from.contentDetails?.videoPublishedAt?.parseYoutubeDateTime() ?: return null
        return playListItem
    }

}