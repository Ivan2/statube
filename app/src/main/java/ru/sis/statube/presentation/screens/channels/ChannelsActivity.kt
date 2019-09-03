package ru.sis.statube.presentation.screens.channels

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_channels.*
import ru.sis.statube.R
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.additional.string
import ru.sis.statube.model.Channels
import ru.sis.statube.presentation.custom.SkeletonViewController
import ru.sis.statube.presentation.screens.channel.ChannelActivity
import java.util.*

class ChannelsActivity : AppCompatActivity() {

    private enum class State {
        LOADING,
        DATA_NOT_FOUND,
        FAVOURITE_DATA_NOT_FOUND,
        LIST
    }

    private val presenter = ChannelsPresenter()

    private val channelsKey = "CHANNELS_KEY"
    private val channelIdLoadingKey = "CHANNEL_ID_LOADING_KEY"

    private lateinit var adapter: ChannelsListAdapter
    private var pageToken: String? = null
    private var searchText = ""
    private var timer: Timer? = null
    private var isRestoreSavedState = false
    private var selectedChannelId: String? = null

    private lateinit var skeletonViewController: SkeletonViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        skeletonViewController = SkeletonViewController(null, vRecyclerView, vDataNotFoundLayout, vSkeletonLayout)
        skeletonViewController.inflateSkeleton(R.layout.list_item_channel_skeleton, 10)

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
            selectedChannelId = channel.id
            loadChannel()
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

        vSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let { text ->
                    vClearButton.visibility = if (text.isEmpty()) View.INVISIBLE else View.VISIBLE
                    searchText = text
                    if (isRestoreSavedState)
                        return
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
        })
        vClearButton.visibility = View.INVISIBLE

        if (savedInstanceState != null && savedInstanceState.containsKey(channelsKey)) {
            val channels = savedInstanceState.getSerializable(channelsKey) as? Channels ?: throw Exception()
            onChannelsLoaded(searchText, channels)
        } else {
            loadFavouriteChannels()
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(channelIdLoadingKey)) {
            selectedChannelId = savedInstanceState.getString(channelIdLoadingKey)
            loadChannel()
        }
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
                skeletonViewController.state = SkeletonViewController.State.SKELETON
            }
            State.DATA_NOT_FOUND -> {
                skeletonViewController.state = SkeletonViewController.State.EMPTY
                vDataNotFoundTextView.text = string(R.string.channels_not_found)
            }
            State.FAVOURITE_DATA_NOT_FOUND -> {
                skeletonViewController.state = SkeletonViewController.State.EMPTY
                vDataNotFoundTextView.text = string(R.string.favourite_channels_not_found)
            }
            State.LIST -> {
                skeletonViewController.state = SkeletonViewController.State.CONTENT
            }
        }
    }

    private fun loadChannel() {
        selectedChannelId?.let { channelId ->
            presenter.loadChannel(this, channelId) {
                if (!this.isDestroyed) {
                    selectedChannelId = null
                    val intent = Intent(this, ChannelActivity::class.java)
                    intent.putExtra(CHANNEL_DATA_KEY, it)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        selectedChannelId?.let {
            outState.putString(channelIdLoadingKey, it)
        }
        if (searchText.isNotEmpty()) {
            outState.putSerializable(channelsKey, Channels().apply {
                this.nextPageToken = pageToken
                this.channelList = adapter.getChannels()
            })
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isRestoreSavedState = true
        super.onRestoreInstanceState(savedInstanceState)
        isRestoreSavedState = false
    }

}
