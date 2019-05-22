package ru.sis.statube.presentation.screens.videos

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_videos.*
import net.cachapa.expandablelayout.ExpandableLayout
import ru.sis.statube.R
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.additional.string
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Video
import kotlin.collections.ArrayList

class VideosActivity : AppCompatActivity() {

    private val presenter = VideosPresenter()

    private lateinit var channel: Channel
    private lateinit var adapter: VideosListAdapter
    private val videoList = ArrayList<Video>()

    private var sortDirection = 1
    private var sortMode: SortMode = SortMode.DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        vBackButton.setOnClickListener {
            onBackPressed()
        }

        channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        vLastUpdateView.isLoading = false
        updateVideosLastUpdatedDateTime()
        vLastUpdateView.onUpdateClickListener = {
            MaterialDialog(this).show {
                title(R.string.dialog_long_loading_title)
                message(R.string.dialog_long_loading_message)
                negativeButton(R.string.dialog_long_loading_cancel)
                positiveButton(R.string.dialog_long_loading_continue) {
                    loadVideos()
                }
            }
        }

        vPeriodChooser.onBeginDateChoose = { loadVideosLocal() }
        vPeriodChooser.onEndDateChoose = { loadVideosLocal() }

        vExpandableLayout.setOnExpansionUpdateListener { expansionFraction, state ->
            val color = Color.argb((expansionFraction * 30).toInt(), 0, 0, 0)
            vLockLayout.setBackgroundColor(color)
            if (state == ExpandableLayout.State.COLLAPSED) {
                vLockLayout.visibility = View.INVISIBLE
            }
        }

        vLockLayout.visibility = View.INVISIBLE

        vSortLayout0.setOnClickListener {
            val layoutParams = vFilterLayout.layoutParams
            layoutParams.width = vFilterLayout0.measuredWidth
            //layoutParams.height = vFilterLayout0.measuredHeight
            vFilterLayout.layoutParams = layoutParams

            val filterLayoutPos = IntArray(2)
            vFilterLayout0.getLocationInWindow(filterLayoutPos)

            val lockLayoutPos = IntArray(2)
            vLockLayout.getLocationInWindow(lockLayoutPos)

            vFilterLayout.x = (filterLayoutPos[0] - lockLayoutPos[0]).toFloat()
            vFilterLayout.y = (filterLayoutPos[1] - lockLayoutPos[1]).toFloat()

            vLockLayout.visibility = View.VISIBLE

            vExpandableLayout.expand()
        }

        vLockLayout.setOnClickListener {
            vExpandableLayout.collapse()
        }

        vSortByDateButton.setOnClickListener { updateSortMode(SortMode.DATE) }
        vSortByViewsButton.setOnClickListener { updateSortMode(SortMode.VIEWS) }
        vSortByLikesButton.setOnClickListener { updateSortMode(SortMode.LIKES) }
        vSortByDislikesButton.setOnClickListener { updateSortMode(SortMode.DISLIKES) }
        vSortByCommentsButton.setOnClickListener { updateSortMode(SortMode.COMMENTS) }
        vSortByLikesDislikesButton.setOnClickListener { updateSortMode(SortMode.LIKES_DISLIKES) }

        vSortDirectionButton.setOnClickListener {
            sortDirection = 1 - sortDirection
            updateSortDirectionView()
            updateVideos()
        }
        vSortDirectionButton0.setOnClickListener {
            vSortDirectionButton.performClick()
        }

        updateSortMode(sortMode)
        updateSortDirectionView()

        adapter = VideosListAdapter()

        vRecyclerView.layoutManager = LinearLayoutManager(this@VideosActivity)
        vRecyclerView.adapter = adapter

        loadVideosLocal()
    }

    private fun updateSortMode(sortMode: SortMode) {
        this.sortMode = sortMode
        vSortTextView.text = when (sortMode) {
            SortMode.DATE -> string(R.string.videos_date)
            SortMode.VIEWS -> string(R.string.videos_views)
            SortMode.LIKES -> string(R.string.videos_likes)
            SortMode.DISLIKES -> string(R.string.videos_dislikes)
            SortMode.COMMENTS -> string(R.string.videos_comments)
            SortMode.LIKES_DISLIKES -> string(R.string.videos_likes_dislikes)
        }
        vSortTextView0.text = vSortTextView.text
        vExpandableLayout.collapse()
        updateVideos()
    }

    private fun updateSortDirectionView() {
        val drawableId = if (sortDirection == 0)
            R.drawable.ic_sort_direction_increase
        else
            R.drawable.ic_sort_direction_decrease
        vSortDirectionButton.setImageResource(drawableId)
        vSortDirectionButton0.setImageResource(drawableId)
    }

    private fun updateVideosLastUpdatedDateTime() {
        channel.uploads?.let { uploads ->
            presenter.loadVideosLastUpdatedDateTime(uploads) { statisticsLastUpdated ->
                vLastUpdateView.update(statisticsLastUpdated?.date, statisticsLastUpdated?.beginDate, statisticsLastUpdated?.endDate)
            }
        }
    }

    private fun loadVideos() {
        channel.uploads?.let { uploads ->
            vLastUpdateView.isLoading = true
            presenter.loadVideos(this, uploads, vPeriodChooser.beginDate, vPeriodChooser.endDate, channel.id) { videoList ->
                videoList?.let {
                    this.videoList.clear()
                    this.videoList.addAll(videoList)
                    vLastUpdateView.isLoading = false
                    updateVideosLastUpdatedDateTime()
                    updateVideos()
                }
            }
        }
    }

    private fun loadVideosLocal() {
        presenter.loadVideosLocal(channel.id, vPeriodChooser.beginDate, vPeriodChooser.endDate) { videoList ->
            videoList?.let {
                this.videoList.clear()
                this.videoList.addAll(videoList)
                updateVideos()
            }
        }
    }

    private fun updateVideos() {
        presenter.sortVideos(videoList, sortMode, sortDirection) {
            adapter.updateVideos(videoList)
        }
    }

}
