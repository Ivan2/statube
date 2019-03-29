package ru.sis.statube.interactor

import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.additional.YOUTUBE_DATA_API_KEY
import ru.sis.statube.additional.YOUTUBE_DATA_API_URL
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Channels
import ru.sis.statube.net.OkRequest
import ru.sis.statube.net.response.json.channel.ChannelSearchListResponse
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

    private val searchPath = "search?key=%s&q=%s&part=id,snippet&order=relevance&maxResults=30&type=channel"
    private val searchWithTokenPath = "search?key=%s&q=%s&part=id,snippet&order=relevance&maxResults=30&type=channel&pageToken=%s"

    fun searchAsync(text: String, pageToken: String? = null) = GlobalScope.async {
        val url = if (pageToken == null)
            "$YOUTUBE_DATA_API_URL${String.format(searchPath, YOUTUBE_DATA_API_KEY, text)}"
        else
            "$YOUTUBE_DATA_API_URL${String.format(searchWithTokenPath, YOUTUBE_DATA_API_KEY, text, pageToken)}"
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

}