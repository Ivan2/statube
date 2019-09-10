package ru.sis.statube.presentation.screens.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_video.*
import ru.sis.statube.R
import ru.sis.statube.additional.*
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.activity.BaseActivity
import java.io.File

class VideoActivity : BaseActivity() {

    override val presenter = VideoPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        vBackButton.setOnClickListener {
            onBackPressed()
        }

        val video = intent?.getSerializableExtra(VIDEO_DATA_KEY) as? Video ?: throw Exception()

        val videoImageFile = File(filesDir, VIDEO_IMAGE_FILE_NAME)
        if (videoImageFile.exists()) {
            vVideoImageView.visibility = View.VISIBLE
            vVideoImageView.loadBanner(videoImageFile)
        } else {
            vVideoImageView.visibility = View.GONE
        }

        vTitleTextView.text = video.title ?: ""
        vDescriptionTextView.text = video.description ?: ""
        vIdTextView.text = video.id
        vScheduledStartDateTextView.text = video.scheduledStartTime?.formatVideoDateTime() ?: "-"
        vStartDateTextView.text = video.actualStartTime?.formatVideoDateTime() ?: "-"
        vEndDateTextView.text = video.actualEndTime?.formatVideoDateTime() ?: "-"
        vPublishedAtTextView.text = video.publishedAt.formatVideoDateTime()
        vDurationTextView.text = video.duration?.formatDuration() ?: "?"
        vViewCountTextView.text = video.viewCount?.format() ?: "?"
        vLikeCountTextView.text = video.likeCount?.format() ?: "?"
        vDislikeCountTextView.text = video.dislikeCount?.format() ?: "?"
        vCommentCountTextView.text = video.commentCount?.format() ?: "?"
        vTagsTextView.text = video.tags?.joinToString(separator = ", ") ?: "?"

        vDescriptionTextView.collapseExpandView = vExpandDescriptionButton
        vExpandDescriptionButton.setOnClickListener {
            vDescriptionTextView.toggle()
            vExpandDescriptionButton.text = string(if (vDescriptionTextView.isExpanded)
                R.string.collapse_text else R.string.expand_text)
        }

        vMoveToYoutubeLayout.setOnClickListener {
            val url = String.format(YOUTUBE_OPEN_VIDEO_URL, video.id)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

}
