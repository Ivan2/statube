package ru.sis.statube.db.mapper

import ru.sis.statube.db.entity.ChannelEntity
import ru.sis.statube.model.Channel

class ChannelEntityMapper {

    fun map(from: ChannelEntity): Channel {
        val channel = Channel()
        channel.id = from.id
        channel.title = from.title
        channel.description = from.description
        channel.thumbnail = from.thumbnail
        return channel
    }

    fun reverseMap(from: Channel): ChannelEntity {
        val channel = ChannelEntity()
        channel.id = from.id
        channel.title = from.title
        channel.description = from.description
        channel.thumbnail = from.thumbnail
        return channel
    }

}