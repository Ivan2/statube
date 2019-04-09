package ru.sis.statube.presentation.screens.channel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_channel.*
import ru.sis.statube.R
import ru.sis.statube.additional.*
import ru.sis.statube.model.Channel
import ru.sis.statube.presentation.screens.statistics.StatisticsActivity
import java.io.File

class ChannelActivity : AppCompatActivity() {

    private val presenter = ChannelPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        vBackButton.setOnClickListener {
            onBackPressed()
        }

        val channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        val bannerImageFile = File(filesDir, BANNER_IMAGE_FILE_NAME)
        if (bannerImageFile.exists()) {
            vBannerImageView.visibility = View.VISIBLE
            vBannerImageView.loadBanner(bannerImageFile)
        } else {
            vBannerImageView.visibility = View.GONE
        }

        vImageView.loadThumbnail(channel.thumbnail)
        vIdTextView.text = channel.id
        vTitleTextView.text = channel.title ?: ""
        vDescriptionTextView.text = channel.description ?: ""
        vPublishedAtTextView.text = channel.publishedAt?.formatPublishedAt() ?: "?"
        vCountryTextView.text = channel.country ?: "?"
        vViewCountTextView.text = channel.viewCount?.format() ?: "?"
        vSubscriberCountTextView.text = channel.subscriberCount?.format() ?: "?"
        vVideoCountTextView.text = channel.videoCount?.format() ?: "?"
        vKeywordsTextView.text = channel.keywords ?: "?"
        updateFavouriteButton(channel.isFavourite)

        vFavouriteButton.setOnClickListener {
            channel.isFavourite = !channel.isFavourite
            presenter.changeFavourite(channel)
            updateFavouriteButton(channel.isFavourite)
        }

        vMoveToYoutubeLayout.setOnClickListener {
            val url = String.format(YOUTUBE_OPEN_CHANNEL_URL, channel.id)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        vMoveToStatisticsLayout.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.putExtra(CHANNEL_DATA_KEY, channel)
            startActivity(intent)
        }
    }

    private fun updateFavouriteButton(isFavourite: Boolean) {
        if (isFavourite) {
            vFavouriteButton.setImageResource(R.drawable.ic_star_black_24dp)
            vFavouriteButton.setColorFilter(color(R.color.favourite))
        } else {
            vFavouriteButton.setImageResource(R.drawable.ic_star_border_black_24dp)
            vFavouriteButton.setColorFilter(color(R.color.not_favourite))
        }
    }

}
