package ru.sis.statube.presentation.screens.channels

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_channels.*
import ru.sis.statube.R
import ru.sis.statube.additional.string
import ru.sis.statube.model.Channels
import java.util.*

class ChannelsActivity : AppCompatActivity() {

    private enum class State {
        LOADING,
        DATA_NOT_FOUND,
        FAVOURITE_DATA_NOT_FOUND,
        LIST
    }

    private val presenter = ChannelsPresenter()

    private lateinit var adapter: ChannelsListAdapter
    private var pageToken: String? = null
    private var searchText = ""
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        adapter = ChannelsListAdapter({
            pageToken?.let {
                presenter.searchChannels(this, searchText, it) { text, channels ->
                    if (text == searchText) {
                        pageToken = channels.nextPageToken
                        adapter.addChannels(channels.channelList, pageToken != null)
                    }
                }
            }
        }, { channel ->
            presenter.loadChannel(this, channel.id) {}
        }, { channel ->
            presenter.changeFavourite(channel)
            if (searchText.isEmpty()) {
                if (channel.isFavourite) {
                    adapter.addChannel(channel)
                } else {
                    adapter.removeChannel(channel)
                }
                setListViewState(if (adapter.itemCount == 0) State.FAVOURITE_DATA_NOT_FOUND else State.LIST)
            }
        })

        vRecyclerView.layoutManager = LinearLayoutManager(this@ChannelsActivity)
        vRecyclerView.adapter = adapter

        vClearButton.setOnClickListener {
            vSearchEditText.setText("")
        }

        vSearchEditText.addTextChangedListener {
            it?.toString()?.let { text ->
                searchText = text
                timer?.cancel()
                if (text.isEmpty()) {
                    loadFavouriteChannels()
                } else {
                    timer = Timer().apply {
                        schedule(object : TimerTask() {
                            override fun run() {
                                runOnUiThread {
                                    setListViewState(State.LOADING)
                                    adapter.clear()
                                    presenter.searchChannels(this@ChannelsActivity, text, null, ::onChannelsLoaded)
                                }
                            }
                        }, 800)
                    }
                }
            }
        }

        loadFavouriteChannels()
    }

    private fun loadFavouriteChannels() {
        setListViewState(State.LOADING)
        adapter.clear()
        presenter.loadFavouriteChannels { channels ->
            if (channels.isEmpty()) {
                setListViewState(State.FAVOURITE_DATA_NOT_FOUND)
            } else {
                setListViewState(State.LIST)
                adapter.addChannels(channels, false)
            }
        }
    }

    private fun onChannelsLoaded(text: String, channels: Channels) {
        if (text != searchText)
            return

        val channelList = channels.channelList
        if (channelList.isEmpty()) {
            setListViewState(State.DATA_NOT_FOUND)
        } else {
            setListViewState(State.LIST)

            pageToken = channels.nextPageToken
            adapter.addChannels(channelList, pageToken != null)
        }
    }

    private fun setListViewState(state: State) {
        when (state) {
            State.LOADING -> {
                vRecyclerView.visibility = View.GONE
                vDataLoadingLayout.visibility = View.VISIBLE
                vDataNotFoundLayout.visibility = View.GONE
            }
            State.DATA_NOT_FOUND -> {
                vDataNotFoundTextView.text = string(R.string.channels_not_found)
                vRecyclerView.visibility = View.GONE
                vDataLoadingLayout.visibility = View.GONE
                vDataNotFoundLayout.visibility = View.VISIBLE
            }
            State.FAVOURITE_DATA_NOT_FOUND -> {
                vDataNotFoundTextView.text = string(R.string.favourite_channels_not_found)
                vRecyclerView.visibility = View.GONE
                vDataLoadingLayout.visibility = View.GONE
                vDataNotFoundLayout.visibility = View.VISIBLE
            }
            State.LIST -> {
                vRecyclerView.visibility = View.VISIBLE
                vDataLoadingLayout.visibility = View.GONE
                vDataNotFoundLayout.visibility = View.GONE
            }
        }
    }

}
