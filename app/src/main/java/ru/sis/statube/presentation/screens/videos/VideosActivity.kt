package ru.sis.statube.presentation.screens.videos

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import kotlinx.android.synthetic.main.activity_videos.*
import net.cachapa.expandablelayout.ExpandableLayout
import org.joda.time.DateTime
import ru.sis.statube.R
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.additional.string
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Video
import java.util.*
import kotlin.collections.ArrayList

class VideosActivity : AppCompatActivity() {

    private val presenter = VideosPresenter()

    private lateinit var channel: Channel
    private lateinit var adapter: VideosListAdapter
    private val videoList = ArrayList<Video>()

    private lateinit var beginDate: DateTime
    private lateinit var endDate: DateTime

    private var sortDirection = 1
    private var sortMode: SortMode = SortMode.DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        vVideosRefreshButton.visibility = View.VISIBLE
        vVideosLoadingProgressBar.visibility = View.INVISIBLE
        updateVideosLastUpdatedDateTime()
        vVideosRefreshButton.setOnClickListener {
            MaterialDialog(this).show {
                message(R.string.statistics_dialog_long_loading_message)
                negativeButton(R.string.statistics_dialog_long_loading_cancel)
                positiveButton(R.string.statistics_dialog_long_loading_continue) {
                    loadVideos()
                }
            }
        }

        endDate = DateTime.now()
        beginDate = endDate.minusMonths(2)

        updateBeginDateText()
        updateEndDateText()

        vBeginDateTextView.setOnClickListener {
            showDatePickerDialog(beginDate) { date ->
                beginDate = date
                updateBeginDateText()
                loadVideosLocal()
            }
        }
        vEndDateTextView.setOnClickListener {
            showDatePickerDialog(endDate) { date ->
                endDate = date
                updateEndDateText()
                loadVideosLocal()
            }
        }

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

    private fun showDatePickerDialog(currentDate: DateTime, callback: (dateTime: DateTime) -> Unit) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentDate.millis
        }
        MaterialDialog(this).show {
            datePicker(currentDate = calendar) { _, date ->
                callback(DateTime(date.timeInMillis))
            }
        }
    }

    private fun updateBeginDateText() {
        vBeginDateTextView.text = beginDate.formatPeriod()
    }

    private fun updateEndDateText() {
        vEndDateTextView.text = endDate.formatPeriod()
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
        vVideosUpdatedTextView.text = "?"
        vVideosDatesTextView.text = string(R.string.videos_dates_template, "?", "?")
        channel.uploads?.let { uploads ->
            presenter.loadVideosLastUpdatedDateTime(uploads) { statisticsLastUpdated ->
                vVideosUpdatedTextView.text = statisticsLastUpdated?.date?.formatUpdate() ?: "?"
                vVideosDatesTextView.text = string(R.string.statistics_dates_template,
                    statisticsLastUpdated?.beginDate?.formatPeriod() ?: "?",
                    statisticsLastUpdated?.endDate?.formatPeriod() ?: "?")
            }
        }
    }

    private fun loadVideos() {
        channel.uploads?.let { uploads ->
            vVideosRefreshButton.visibility = View.INVISIBLE
            vVideosLoadingProgressBar.visibility = View.VISIBLE

            presenter.loadVideos(this, uploads, beginDate, endDate, channel.id) { videoList ->
                videoList?.let {
                    this.videoList.clear()
                    this.videoList.addAll(videoList)
                    vVideosRefreshButton.visibility = View.VISIBLE
                    vVideosLoadingProgressBar.visibility = View.INVISIBLE
                    updateVideosLastUpdatedDateTime()
                    updateVideos()
                }
            }
        }
    }

    private fun loadVideosLocal() {
        presenter.loadVideosLocal(channel.id, beginDate, endDate) { videoList ->
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
