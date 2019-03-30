package ru.sis.statube.presentation.screens.channels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_channels.*
import ru.sis.statube.R
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.presentation.screens.channel.ChannelActivity

class ChannelsActivity : AppCompatActivity() {

    private val presenter = ChannelsPresenter()

    private var pageToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        vRecyclerView.layoutManager = LinearLayoutManager(this@ChannelsActivity)

        vSearchButton.setOnClickListener {
            presenter.searchChannels(vSearchEditText.text.toString()) { channels ->
                pageToken = channels.nextPageToken

                vRecyclerView.adapter = ChannelsListAdapter(channels.channelList, { addChannels ->
                    presenter.searchChannels(vSearchEditText.text.toString(), pageToken) { channels ->
                        pageToken = channels.nextPageToken
                        addChannels(channels.channelList)
                    }
                }, { channel ->
                    val intent = Intent(this, ChannelActivity::class.java)
                    intent.putExtra(CHANNEL_DATA_KEY, channel)
                    startActivity(intent)
                })
            }
        }
    }

}
