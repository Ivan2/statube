package ru.sis.statube.interactor

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.additional.YOUTUBE_DATA_API_URL
import ru.sis.statube.db.store.ChannelStore
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Channels
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.youtube.channel.ChannelListResponse
import ru.sis.statube.net.response.json.youtube.search.ChannelSearchListResponse
import ru.sis.statube.net.response.mapper.youtube.ChannelResponseMapper
import ru.sis.statube.net.response.mapper.youtube.ChannelSearchResponseMapper

class ChannelInteractor : Interactor() {

    companion object {
        private var INSTANCE: ChannelInteractor? = null
        fun getInstance(): ChannelInteractor {
            val instance = INSTANCE ?: ChannelInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val searchPath = "search?key=%s&q=%s&part=id,snippet&order=relevance&maxResults=30&type=channel"
    private val searchWithTokenPath = "search?key=%s&q=%s&part=id,snippet&order=relevance&maxResults=30&type=channel&pageToken=%s"
    private val channelPath = "channels?key=%s&id=%s&part=snippet,statistics,brandingSettings,contentDetails"

    fun searchAsync(context: Context, text: String, pageToken: String? = null) = GlobalScope.async {
        val config = loadConfig(context)
        val url = if (pageToken == null)
            "$YOUTUBE_DATA_API_URL${String.format(searchPath, config.youtubeDataApiKey, text)}"
        else
            "$YOUTUBE_DATA_API_URL${String.format(searchWithTokenPath, config.youtubeDataApiKey, text, pageToken)}"
        val response = OkRequest.getInstance().get(url)
        val channelSearchListResponse = Gson().fromJson(response, ChannelSearchListResponse::class.java)

        val mapper = ChannelSearchResponseMapper()
        val channelList = ArrayList<Channel>()
        channelSearchListResponse.items?.forEach { channelSearchResponse ->
            mapper.map(channelSearchResponse)?.let { channel ->
                channelList.add(channel)
            }
        }

        val store = ChannelStore.getInstance()
        channelList.forEach { channel ->
            channel.isFavourite = store.getChannel(channel.id) != null
        }

        Channels().apply {
            this.nextPageToken = channelSearchListResponse.nextPageToken
            this.channelList = channelList
        }
    }

    fun loadAsync(context: Context, channelId: String) = GlobalScope.async {
        val config = loadConfig(context)
        val url = "$YOUTUBE_DATA_API_URL${String.format(channelPath, config.youtubeDataApiKey, channelId)}"
        val response = OkRequest.getInstance().get(url)
        val channelListResponse = Gson().fromJson(response, ChannelListResponse::class.java)

        val mapper = ChannelResponseMapper()
        val channelList = ArrayList<Channel>()
        channelListResponse.items?.forEach { channelResponse ->
            mapper.map(channelResponse)?.let { channel ->
                channelList.add(channel)
            }
        }

        channelList.firstOrNull()?.apply {
            this.isFavourite = ChannelStore.getInstance().getChannel(this.id) != null
        }
    }

    fun loadFavouriteChannelsAsync() = GlobalScope.async {
        ChannelStore.getInstance().getChannels()
    }

    fun changeFavouriteAsync(channel: Channel) = GlobalScope.async {
        if (channel.isFavourite) {
            ChannelStore.getInstance().saveChannel(channel)
        } else {
            ChannelStore.getInstance().deleteChannel(channel)
        }
        Unit
    }

}