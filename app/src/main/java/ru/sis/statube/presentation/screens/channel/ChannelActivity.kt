package ru.sis.statube.presentation.screens.channel

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_channel.*
import org.joda.time.DateTime
import ru.sis.statube.R
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.additional.YOUTUBE_OPEN_CHANNEL_URL
import ru.sis.statube.model.Channel
import ru.sis.statube.presentation.screens.statistics.StatisticsActivity

class ChannelActivity : AppCompatActivity() {

    private val presenter = ChannelPresenter()

    private var imgUrl: String? = null
    private var bannerImgUrl: String? = null
    private var title: String? = null
    private var description: String? = null
    private var publishedAt: DateTime? = null
    private var country: String? = null
    private var viewCount: String? = null
    private var subscriberCount: String? = null
    private var videoCount: String? = null
    private var keywords: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        val channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        update(channel)

        presenter.loadChannel(this, channel.id) { updatedChannel ->
            if (updatedChannel != null)
                update(updatedChannel)
        }
    }

    private fun update(channel: Channel) {
        val bannerImgUrl = channel.brandingSettings?.let { brandingSettings ->
            brandingSettings.bannerMobileHdImageUrl ?:
            brandingSettings.bannerMobileMediumHdImageUrl ?:
            brandingSettings.bannerMobileExtraHdImageUrl ?:
            brandingSettings.bannerMobileLowImageUrl ?:
            brandingSettings.bannerMobileImageUrl ?:
            brandingSettings.bannerImageUrl
        }
        if (this.bannerImgUrl != bannerImgUrl) {
            this.bannerImgUrl = bannerImgUrl
            Glide.with(this)
                .load(bannerImgUrl)
                //.placeholder(R.drawable.product_default)
                .fitCenter()
                .dontAnimate()
                .into(vBannerImageView)
        }

        val imgUrl = channel.thumbnails?.let { thumbnails ->
            thumbnails.high?.url ?: thumbnails.medium?.url ?: thumbnails.default?.url
        }
        if (this.imgUrl != imgUrl) {
            this.imgUrl = imgUrl
            Glide.with(this)
                .load(imgUrl)
                //.placeholder(R.drawable.product_default)
                .centerCrop()
                .dontAnimate()
                .into(vImageView)
        }

        if (title != channel.title) {
            title = channel.title
            vTitleTextView.text = title ?: ""
        }

        if (description != channel.description) {
            description = channel.description
            vDescriptionTextView.text = description ?: ""
        }

        if (publishedAt != channel.publishedAt) {
            publishedAt = channel.publishedAt
            vPublishedAtTextView.text = publishedAt?.toString("dd.MM.YYYY") ?: ""
        }

        if (country != channel.country) {
            country = channel.country
            vCountryTextView.text = country ?: ""
        }

        if (viewCount != channel.statistics?.viewCount) {
            viewCount = channel.statistics?.viewCount
            vViewCountTextView.text = viewCount ?: ""
        }

        if (subscriberCount != channel.statistics?.subscriberCount) {
            subscriberCount = channel.statistics?.subscriberCount
            vSubscriberCountTextView.text = subscriberCount ?: ""
        }

        if (videoCount != channel.statistics?.videoCount) {
            videoCount = channel.statistics?.videoCount
            vVideoCountTextView.text = videoCount ?: ""
        }

        if (keywords != channel.brandingSettings?.keywords) {
            keywords = channel.brandingSettings?.keywords
            vKeywordsTextView.text = keywords ?: ""
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

}
