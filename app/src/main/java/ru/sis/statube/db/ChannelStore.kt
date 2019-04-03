package ru.sis.statube.db

import io.realm.Realm
import ru.sis.statube.model.Channel

class ChannelStore {

    companion object {
        private var INSTANCE: ChannelStore? = null
        fun getInstance(): ChannelStore {
            if (INSTANCE == null)
                INSTANCE = ChannelStore()
            return INSTANCE!!
        }
    }

    fun getChannels(): List<Channel> = Realm.getDefaultInstance().use { realm ->
        val channels = ArrayList<Channel>()
        val entities = realm.where(ChannelEntity::class.java)
                .findAll()
        val mapper = ChannelEntityMapper()
        for (entity in entities) {
            val channel = mapper.map(entity)
            channel.isFavourite = true
            channels.add(channel)
        }
        channels
    }

    fun getChannel(channelId: String): Channel? = Realm.getDefaultInstance().use { realm ->
        val entities = realm.where(ChannelEntity::class.java)
            .equalTo("id", channelId)
            .findAll()
        entities.firstOrNull()?.let {
            ChannelEntityMapper().map(it).apply {
                isFavourite = true
            }
        }
    }

    fun saveChannel(channel: Channel) = Realm.getDefaultInstance().use { realm ->
        channel.isFavourite = true
        val entity = ChannelEntityMapper().reverseMap(channel)
        realm.executeTransaction {
            realm.insertOrUpdate(entity)
        }
        channel
    }

    fun deleteChannel(channel: Channel) = Realm.getDefaultInstance().use { realm ->
        val entity = ChannelEntityMapper().reverseMap(channel)
        realm.executeTransaction {
            realm.where(ChannelEntity::class.java)
                .equalTo("id", entity.id)
                .findAll()
                .deleteAllFromRealm()
        }
    }

}