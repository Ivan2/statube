package ru.sis.statube.interactor

import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.additional.YOUTUBE_DATA_API_KEY
import ru.sis.statube.additional.YOUTUBE_DATA_API_URL
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Channels
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.channel.ChannelListResponse
import ru.sis.statube.net.response.json.search.ChannelSearchListResponse
import ru.sis.statube.net.response.mapper.ChannelResponseMapper
import ru.sis.statube.net.response.mapper.ChannelSearchResponseMapper

class ChannelInteractor {

    companion object {
        private var INSTANCE: ChannelInteractor? = null
        fun getInstance(): ChannelInteractor {
            val instance = INSTANCE ?: ChannelInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val searchPath = "search?key=$YOUTUBE_DATA_API_KEY&q=%s&part=id,snippet&order=relevance&maxResults=30&type=channel"
    private val searchWithTokenPath = "search?key=$YOUTUBE_DATA_API_KEY&q=%s&part=id,snippet&order=relevance&maxResults=30&type=channel&pageToken=%s"
    private val channelPath = "channels?key=$YOUTUBE_DATA_API_KEY&id=%s&part=snippet,statistics,brandingSettings"

    fun searchAsync(text: String, pageToken: String? = null) = GlobalScope.async {
        val url = if (pageToken == null)
            "$YOUTUBE_DATA_API_URL${String.format(searchPath, text)}"
        else
            "$YOUTUBE_DATA_API_URL${String.format(searchWithTokenPath, text, pageToken)}"
        val response = OkRequest.getInstance().get(url)
        val channelSearchListResponse = Gson().fromJson(response, ChannelSearchListResponse::class.java)

        val mapper = ChannelSearchResponseMapper()
        val channelList = ArrayList<Channel>()
        channelSearchListResponse.items?.forEach { channelSearchResponse ->
            mapper.map(channelSearchResponse)?.let { channel ->
                channelList.add(channel)
            }
        }

        Channels().apply {
            this.nextPageToken = channelSearchListResponse.nextPageToken
            this.channelList = channelList
        }
    }

    fun loadAsync(channelId: String) = GlobalScope.async {
        val url = "$YOUTUBE_DATA_API_URL${String.format(channelPath, channelId)}"
        val response = OkRequest.getInstance().get(url)
        val channelListResponse = Gson().fromJson(response, ChannelListResponse::class.java)

        val mapper = ChannelResponseMapper()
        val channelList = ArrayList<Channel>()
        channelListResponse.items?.forEach { channelResponse ->
            mapper.map(channelResponse)?.let { channel ->
                channelList.add(channel)
            }
        }

        channelList.firstOrNull()
    }

}