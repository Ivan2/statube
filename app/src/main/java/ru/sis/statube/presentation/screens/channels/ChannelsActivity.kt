package ru.sis.statube.presentation.screens.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_channels.*
import ru.sis.statube.R
import ru.sis.statube.model.Channels
import java.util.*

class ChannelsActivity : AppCompatActivity() {

    private val presenter = ChannelsPresenter()

    private var pageToken: String? = null

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        vRecyclerView.layoutManager = LinearLayoutManager(this@ChannelsActivity)
        setListViewState(0)

        vClearButton.setOnClickListener {
            vSearchEditText.setText("")
        }

        vSearchEditText.addTextChangedListener {
            timer?.cancel()
            if (vSearchEditText.text.toString().isEmpty()) {
                setListViewState(0)
                return@addTextChangedListener
            }
            timer = Timer().apply {
                val task = object: TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            setListViewState(1)
                            presenter.searchChannels(this@ChannelsActivity, vSearchEditText.text.toString(), null, ::onChannelsLoaded)
                        }
                    }
                }

                schedule(task, 800)
            }
        }
    }

    private fun onChannelsLoaded(text: String, channels: Channels) {
        if (text != vSearchEditText.text.toString())
            return

        val channelList = channels.channelList
        if (channelList.isEmpty()) {
            setListViewState(0)
        } else {
            setListViewState(2)

            pageToken = channels.nextPageToken

            vRecyclerView.adapter = ChannelsListAdapter(channelList, { addChannels ->
                presenter.searchChannels(this, vSearchEditText.text.toString(), pageToken) { text2, channels ->
                    if (text2 != vSearchEditText.text.toString())
                        return@searchChannels
                    pageToken = channels.nextPageToken
                    addChannels(channels.channelList)
                }
            }, { channel ->
                presenter.loadChannel(this, channel.id) {}
            }, { channel ->
                presenter.changeFavourite(channel)
            })
        }
    }

    private fun setListViewState(state: Int) {
        when (state) {
            0 -> {
                vRecyclerView.visibility = View.GONE
                vDataLoadingLayout.visibility = View.GONE
                vDataNotFoundLayout.visibility = View.VISIBLE
            }
            1 -> {
                vRecyclerView.visibility = View.GONE
                vDataLoadingLayout.visibility = View.VISIBLE
                vDataNotFoundLayout.visibility = View.GONE
            }
            2 -> {
                vRecyclerView.visibility = View.VISIBLE
                vDataLoadingLayout.visibility = View.GONE
                vDataNotFoundLayout.visibility = View.GONE
            }
        }
    }

}
