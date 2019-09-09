package ru.sis.statube.presentation.screens.videos

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_videos.*
import org.joda.time.DateTime
import org.joda.time.Duration
import org.supercsv.io.CsvBeanWriter
import org.supercsv.prefs.CsvPreference
import ru.sis.statube.R
import ru.sis.statube.additional.*
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.activity.FullVideoLoadingActivity
import ru.sis.statube.presentation.custom.SkeletonViewController
import java.io.File
import java.io.PrintWriter

class VideosActivity : FullVideoLoadingActivity() {

    override val presenter = VideosPresenter()

    private lateinit var channel: Channel
    private lateinit var adapter: VideosListAdapter
    private val videoList = ArrayList<Video>()
    private val filteredVideoList = ArrayList<Video>()

    private var sortDirection = 1
    private var sortMode: SortMode = SortMode.DATE

    private var durationRange = Pair(0L, 0L)
    private var viewCountRange = Pair(0L, 0L)
    private var likeCountRange = Pair(0L, 0L)
    private var dislikeCountRange = Pair(0L, 0L)
    private var commentCountRange = Pair(0L, 0L)

    private lateinit var skeletonViewController: SkeletonViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: throw Exception()

        skeletonViewController = SkeletonViewController(null, vRecyclerView, vDataNotFoundLayout, vSkeletonLayout)
        skeletonViewController.inflateSkeleton(R.layout.list_item_video_skeleton, 10)

        vBackButton.setOnClickListener {
            onBackPressed()
        }

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

        vSlidingUpPanelLayout.post {
            val height = vSlidingUpPanelLayout.measuredHeight
            val anchorPoint = if (height > 0) pxFromDp(100f) / height else 0.2f
            vSlidingUpPanelLayout.anchorPoint = anchorPoint
        }

        vPeriodChooser.onBeginDateChoose = { loadVideosLocal() }
        vPeriodChooser.onEndDateChoose = { loadVideosLocal() }

        vSortByDateButton.setOnClickListener { updateSortMode(SortMode.DATE) }
        vSortByDurationButton.setOnClickListener { updateSortMode(SortMode.DURATION) }
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

        updateSortMode(sortMode)
        updateSortDirectionView()

        vDurationRange.onValueChanged = { min, max ->
            "${Duration(min).toPeriod().formatDuration()} - ${Duration(max).toPeriod().formatDuration()}"
        }
        vViewCountRange.onValueChanged = { min, max ->
            "${min.format()} - ${max.format()}"
        }
        vLikeCountRange.onValueChanged = { min, max ->
            "${min.format()} - ${max.format()}"
        }
        vDislikeCountRange.onValueChanged = { min, max ->
            "${min.format()} - ${max.format()}"
        }
        vCommentCountRange.onValueChanged = { min, max ->
            "${min.format()} - ${max.format()}"
        }

        vDurationRange.onValueChangeEnded = { min, max ->
            durationRange = Pair(min, max)
            filterVideos()
        }
        vViewCountRange.onValueChangeEnded = { min, max ->
            viewCountRange = Pair(min, max)
            filterVideos()
        }
        vLikeCountRange.onValueChangeEnded = { min, max ->
            likeCountRange = Pair(min, max)
            filterVideos()
        }
        vDislikeCountRange.onValueChangeEnded = { min, max ->
            dislikeCountRange = Pair(min, max)
            filterVideos()
        }
        vCommentCountRange.onValueChangeEnded = { min, max ->
            commentCountRange = Pair(min, max)
            filterVideos()
        }

        adapter = VideosListAdapter { video ->
            loadFullVideo(video)
        }

        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = adapter

        vExportToCSVButton.setOnClickListener {
            exportToCSV()
        }

        loadVideosLocal()
    }

    private fun ImageView.updateSortMode(isSelected: Boolean) {
        this.isSelected = isSelected
        this.imageTintList = ColorStateList.valueOf(color(if (isSelected) R.color.white else R.color.red))
    }

    private fun updateSortMode(sortMode: SortMode) {
        this.sortMode = sortMode
        vSortByDateButton.updateSortMode(sortMode == SortMode.DATE)
        vSortByDurationButton.updateSortMode(sortMode == SortMode.DURATION)
        vSortByViewsButton.updateSortMode(sortMode == SortMode.VIEWS)
        vSortByLikesButton.updateSortMode(sortMode == SortMode.LIKES)
        vSortByDislikesButton.updateSortMode(sortMode == SortMode.DISLIKES)
        vSortByCommentsButton.updateSortMode(sortMode == SortMode.COMMENTS)
        vSortByLikesDislikesButton.updateSortMode(sortMode == SortMode.LIKES_DISLIKES)
        updateVideos()
    }

    private fun updateSortDirectionView() {
        val drawableId = if (sortDirection == 0)
            R.drawable.ic_sort_direction_increase
        else
            R.drawable.ic_sort_direction_decrease
        vSortDirectionButton.setImageResource(drawableId)
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
            skeletonViewController.state = SkeletonViewController.State.SKELETON
            presenter.loadVideos(this, uploads, vPeriodChooser.beginDate, vPeriodChooser.endDate, channel.id) { videoList ->
                this.videoList.clear()
                this.filteredVideoList.clear()
                this.videoList.addAll(videoList)
                vLastUpdateView.isLoading = false
                updateVideosLastUpdatedDateTime()
                updateFilters()
                filterVideos()
            }
        }
    }

    private fun loadVideosLocal() {
        skeletonViewController.state = SkeletonViewController.State.SKELETON
        presenter.loadVideosLocal(channel.id, vPeriodChooser.beginDate, vPeriodChooser.endDate) { videoList ->
            this.videoList.clear()
            this.filteredVideoList.clear()
            this.videoList.addAll(videoList)
            updateFilters()
            filterVideos()
        }
    }

    private fun filterVideos() {
        filteredVideoList.clear()
        filteredVideoList.addAll(videoList.filter { video ->
            video.duration?.toStandardDuration()?.millis in durationRange.first..durationRange.second &&
                    video.viewCount in viewCountRange.first..viewCountRange.second &&
                    video.likeCount in likeCountRange.first..likeCountRange.second &&
                    video.dislikeCount in dislikeCountRange.first..dislikeCountRange.second &&
                    video.commentCount in commentCountRange.first..commentCountRange.second
        })
        updateVideos()
    }

    private fun updateFilters() {
        if (videoList.isNotEmpty()) {
            val minMaxDuration = videoList.mapNotNull { video -> video.duration?.toStandardDuration()?.millis }.getMinAndMaxValue() ?: Pair(0L, 0L)
            val minMaxViewCount = videoList.mapNotNull { video -> video.viewCount }.getMinAndMaxValue() ?: Pair(0L, 0L)
            val minMaxLikeCount = videoList.mapNotNull { video -> video.likeCount }.getMinAndMaxValue() ?: Pair(0L, 0L)
            val minMaxDislikeCount = videoList.mapNotNull { video -> video.dislikeCount }.getMinAndMaxValue() ?: Pair(0L, 0L)
            val minMaxCommentCount = videoList.mapNotNull { video -> video.commentCount }.getMinAndMaxValue() ?: Pair(0L, 0L)

            vDurationRange.setMinMax(minMaxDuration.first, minMaxDuration.second)
            vViewCountRange.setMinMax(minMaxViewCount.first, minMaxViewCount.second)
            vLikeCountRange.setMinMax(minMaxLikeCount.first, minMaxLikeCount.second)
            vDislikeCountRange.setMinMax(minMaxDislikeCount.first, minMaxDislikeCount.second)
            vCommentCountRange.setMinMax(minMaxCommentCount.first, minMaxCommentCount.second)
        }
    }

    private fun updateVideos() {
        skeletonViewController.state = if (filteredVideoList.isEmpty())
            SkeletonViewController.State.EMPTY else SkeletonViewController.State.CONTENT
        presenter.sortVideos(filteredVideoList, sortMode, sortDirection) {
            adapter.updateVideos(filteredVideoList)
        }
    }

    private fun Collection<Long>.getMinAndMaxValue(): Pair<Long, Long>? {
        var minCount: Long? = null
        var maxCount: Long? = null

        this.forEach { value ->
            minCount?.let {
                if (value < it)
                    minCount = value
            } ?: run {
                minCount = value
            }
            maxCount?.let {
                if (value > it)
                    maxCount = value
            } ?: run {
                maxCount = value
            }
        }

        return minCount?.let {
            maxCount?.let { maxCount ->
                Pair(it, maxCount)
            }
        }
    }

    private fun exportToCSV() {
        try {
            if (filteredVideoList.isNotEmpty()) {
                val exportedDir = File(filesDir, "exported")
                exportedDir.mkdirs()
                exportedDir.listFiles()?.forEach { it.delete() }
                val file = File(exportedDir, "StaTube_videos_${DateTime.now().toString("dd-MM-yyyy_HH-mm-ss")}.csv")
                file.createNewFile()

                val writer = PrintWriter(file)
                val csvBeanWriter = CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE)

                val header = arrayOf("channelId", "id", "date", "title", "duration",
                    "views", "likes", "dislikes", "comments")
                csvBeanWriter.writeHeader(*header)

                filteredVideoList.forEach { video ->
                    val raw = arrayOf(video.channelId, video.id, video.publishedAt.formatYoutubeDateTime(),
                        video.title ?: "?", video.duration?.formatDuration() ?: "?",
                        video.viewCount?.toString() ?: "?", video.likeCount?.toString() ?: "?",
                        video.dislikeCount?.toString() ?: "?", video.commentCount?.toString() ?: "?")
                    csvBeanWriter.writeHeader(*raw)
                }

                csvBeanWriter.close()
                writer.close()

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/csv"
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val uri = FileProvider.getUriForFile(this, string(R.string.file_provider_authority), file)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, string(R.string.choose_program)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
