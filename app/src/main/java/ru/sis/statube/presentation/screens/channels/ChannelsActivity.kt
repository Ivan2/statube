package ru.sis.statube.presentation.screens.channels

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_channels.*
import ru.sis.statube.R
import ru.sis.statube.additional.YOUTUBE_OPEN_CHANNEL_URL

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
                    val url = String.format(YOUTUBE_OPEN_CHANNEL_URL, channel.id)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                })
            }
        }
    }

}
