package ru.sis.statube.db

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

    /*fun map(from: List<ChannelEntity>): List<Channel> {
        val list: MutableList<Card> = ArrayList()
        from.forEach { obj ->
            val obj2 = map(obj)
            if (obj2 != null)
                list.add(obj2)
        }
        return list
    }*/

    fun reverseMap(from: Channel): ChannelEntity {
        val channel = ChannelEntity()
        channel.id = from.id
        channel.title = from.title
        channel.description = from.description
        channel.thumbnail = from.thumbnail
        return channel
    }

    /*fun reverseMap(from: List<Card>): List<CardEntity> {
        val list: MutableList<CardEntity> = ArrayList()
        from.forEach { obj ->
            list.add(reverseMap(obj))
        }
        return list
    }*/

}