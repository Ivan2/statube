package ru.sis.statube.presentation.screens.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_statistics.*
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Seconds
import ru.sis.statube.R
import ru.sis.statube.additional.*
import ru.sis.statube.model.Channel
import ru.sis.statube.model.DataDaily
import ru.sis.statube.model.GeneralStatistics
import ru.sis.statube.model.Video
import java.util.*
import kotlin.collections.ArrayList

class StatisticsActivity : AppCompatActivity() {

    private val presenter = StatisticsPresenter()

    private lateinit var channel: Channel

    private var dataDailyList: List<DataDaily>? = null
    private var videoList: List<Video> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        vBackButton.setOnClickListener {
            onBackPressed()
        }

        channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        vGeneralStatisticsLastUpdateView.isPeriodVisible = false
        vGeneralStatisticsLastUpdateView.isLoading = false
        updateGeneralStatisticsLastUpdatedDateTime()
        vGeneralStatisticsLastUpdateView.onUpdateClickListener = {
            updateGeneralStatistics()
        }

        vGeneralStatisticsChartTypeButtonGroup.setOnPositionChangedListener { _, currentPosition, _ ->
            updateGeneralStatisticsChart(currentPosition)
        }
        vGeneralStatisticsSubsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateGeneralStatisticsChart()
        }
        vGeneralStatisticsViewsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateGeneralStatisticsChart()
        }


        vVideosStatisticsLastUpdateView.isLoading = false
        updateVideosStatisticsLastUpdatedDateTime()
        vVideosStatisticsLastUpdateView.onUpdateClickListener = {
            MaterialDialog(this).show {
                title(R.string.dialog_long_loading_title)
                message(R.string.dialog_long_loading_message)
                negativeButton(R.string.dialog_long_loading_cancel)
                positiveButton(R.string.dialog_long_loading_continue) {
                    updateVideosStatistics()
                }
            }
        }

        vVideosStatisticsPeriodChooser.onBeginDateChoose = { updateVideosStatisticsLocal() }
        vVideosStatisticsPeriodChooser.onEndDateChoose = { updateVideosStatisticsLocal() }

        vVideosStatisticsViewsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateVideosStatisticsChart()
        }
        vVideosStatisticsCommentsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateVideosStatisticsChart()
        }
        vVideosStatisticsLikesCheckBox.setOnCheckedChangeListener { _, _ ->
            updateVideosStatisticsChart()
        }
        vVideosStatisticsDislikesCheckBox.setOnCheckedChangeListener { _, _ ->
            updateVideosStatisticsChart()
        }

        prepareChart(vGeneralStatisticsLineChart)
        prepareChart(vVideosStatisticsLineChart)

        if (channel.uploads.isNullOrEmpty()) {
            vVideosStatisticsLayout.visibility = View.GONE
        }

        updateGeneralStatisticsLocal()
        updateVideosStatisticsLocal()
    }

    private fun prepareChart(chart: LineChart) {
        chart.isDoubleTapToZoomEnabled = true
        chart.setPinchZoom(true)
        chart.isScaleXEnabled = true
        chart.isScaleYEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.textSize = 9f
        chart.axisLeft.textSize = 9f
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.setDrawMarkers(true)
        chart.legend.isEnabled = false
        chart.setNoDataText(string(R.string.statistics_chart_no_data))
        chart.setNoDataTextColor(Color.RED)
        chart.setMaxVisibleValueCount(14)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.data = null
    }

    private fun updateGeneralStatisticsLastUpdatedDateTime() {
        presenter.loadGeneralStatisticsLastUpdatedDateTime(channel.id) { statisticsLastUpdated ->
            vGeneralStatisticsLastUpdateView.update(statisticsLastUpdated?.date)
        }
    }

    private fun updateVideosStatisticsLastUpdatedDateTime() {
        channel.uploads?.let { uploads ->
            presenter.loadVideosStatisticsLastUpdatedDateTime(uploads) { statisticsLastUpdated ->
                vVideosStatisticsLastUpdateView.update(statisticsLastUpdated?.date, statisticsLastUpdated?.beginDate, statisticsLastUpdated?.endDate)
            }
        }
    }

    private fun updateGeneralStatistics() {
        vGeneralStatisticsLastUpdateView.isLoading = true
        presenter.loadGeneralStatistics(this, channel.id, { statistics ->
            vGeneralStatisticsLastUpdateView.isLoading = false
            updateGeneralStatisticsTable(statistics)
            updateGeneralStatisticsChart()
            updateGeneralStatisticsLastUpdatedDateTime()
        }, {
            vGeneralStatisticsLastUpdateView.isLoading = false
        })
    }

    private fun updateGeneralStatisticsLocal() {
        vGeneralStatisticsLastUpdateView.isLoading = true
        presenter.loadGeneralStatisticsLocal(channel.id) { statistics ->
            vGeneralStatisticsLastUpdateView.isLoading = false
            updateGeneralStatisticsTable(statistics)
            updateGeneralStatisticsChart()
        }
    }

    private fun updateGeneralStatisticsTable(statistics: GeneralStatistics?) {
        vAvgDailySubsTextView.text = statistics?.avgDailySubs?.format() ?: "?"
        vAvgDailyViewsTextView.text = statistics?.avgDailyViews?.format() ?: "?"
        vSubs14TextView.text = statistics?.subsByPeriod?.by14?.format() ?: "?"
        vViews14TextView.text = statistics?.viewsByPeriod?.by14?.format() ?: "?"
        vSubs30TextView.text = statistics?.subsByPeriod?.by30?.format() ?: "?"
        vViews30TextView.text = statistics?.viewsByPeriod?.by30?.format() ?: "?"
        vSubs60TextView.text = statistics?.subsByPeriod?.by60?.format() ?: "?"
        vViews60TextView.text = statistics?.viewsByPeriod?.by60?.format() ?: "?"
        vSubs90TextView.text = statistics?.subsByPeriod?.by90?.format() ?: "?"
        vViews90TextView.text = statistics?.viewsByPeriod?.by90?.format() ?: "?"
        vSubs180TextView.text = statistics?.subsByPeriod?.by180?.format() ?: "?"
        vViews180TextView.text = statistics?.viewsByPeriod?.by180?.format() ?: "?"
        vSubs365TextView.text = statistics?.subsByPeriod?.by365?.format() ?: "?"
        vViews365TextView.text = statistics?.viewsByPeriod?.by365?.format() ?: "?"

        statistics?.dataDailyList?.let { dataDailyList ->
            this.dataDailyList = dataDailyList
        }
    }

    private fun updateVideosStatistics() {
        channel.uploads?.let { uploads ->
            vVideosStatisticsLastUpdateView.isLoading = true

            presenter.loadVideosStatistics(this, uploads, vVideosStatisticsPeriodChooser.beginDate, vVideosStatisticsPeriodChooser.endDate, channel.id) { videoList ->
                vVideosStatisticsLastUpdateView.isLoading = false
                this.videoList = videoList
                updateVideosStatisticsTable()
                updateVideosStatisticsChart()
                updateVideosStatisticsLastUpdatedDateTime()
            }
        }
    }

    private fun updateVideosStatisticsLocal() {
        vVideosStatisticsLastUpdateView.isLoading = true
        presenter.loadVideosStatisticsLocal(channel.id, vVideosStatisticsPeriodChooser.beginDate, vVideosStatisticsPeriodChooser.endDate) { videoList ->
            vVideosStatisticsLastUpdateView.isLoading = false
            this.videoList = videoList
            updateVideosStatisticsTable()
            updateVideosStatisticsChart()
        }
    }

    private fun updateVideosStatisticsTable() {
        val now = DateTime.now()

        fun DateTime.daysBeforeNow(): Int = Days.daysBetween(this, now).days

        fun sum(v1: Long?, v2: Long?): Long? {
            if (v1 == null && v2 == null)
                return null
            return (v1 ?: 0L) + (v2 ?: 0L)
        }

        val viewCount = Array<Long?>(7) { null }
        val likeCount = Array<Long?>(7) { null }
        val dislikeCount = Array<Long?>(7) { null }
        val commentCount = Array<Long?>(7) { null }

        videoList.forEach { video ->
            val days = video.publishedAt.daysBeforeNow()

            viewCount[0] = sum(viewCount[0], video.viewCount)
            likeCount[0] = sum(likeCount[0], video.likeCount)
            dislikeCount[0] = sum(dislikeCount[0], video.dislikeCount)
            commentCount[0] = sum(commentCount[0], video.commentCount)
            if (days <= 14) {
                viewCount[1] = sum(viewCount[1], video.viewCount)
                likeCount[1] = sum(likeCount[1], video.likeCount)
                dislikeCount[1] = sum(dislikeCount[1], video.dislikeCount)
                commentCount[1] = sum(commentCount[1], video.commentCount)
            }
            if (days <= 30) {
                viewCount[2] = sum(viewCount[2], video.viewCount)
                likeCount[2] = sum(likeCount[2], video.likeCount)
                dislikeCount[2] = sum(dislikeCount[2], video.dislikeCount)
                commentCount[2] = sum(commentCount[2], video.commentCount)
            }
            if (days <= 60) {
                viewCount[3] = sum(viewCount[3], video.viewCount)
                likeCount[3] = sum(likeCount[3], video.likeCount)
                dislikeCount[3] = sum(dislikeCount[3], video.dislikeCount)
                commentCount[3] = sum(commentCount[3], video.commentCount)
            }
            if (days <= 90) {
                viewCount[4] = sum(viewCount[4], video.viewCount)
                likeCount[4] = sum(likeCount[4], video.likeCount)
                dislikeCount[4] = sum(dislikeCount[4], video.dislikeCount)
                commentCount[4] = sum(commentCount[4], video.commentCount)
            }
            if (days <= 180) {
                viewCount[5] = sum(viewCount[5], video.viewCount)
                likeCount[5] = sum(likeCount[5], video.likeCount)
                dislikeCount[5] = sum(dislikeCount[5], video.dislikeCount)
                commentCount[5] = sum(commentCount[5], video.commentCount)
            }
            if (days <= 365) {
                viewCount[6] = sum(viewCount[6], video.viewCount)
                likeCount[6] = sum(likeCount[6], video.likeCount)
                dislikeCount[6] = sum(dislikeCount[6], video.dislikeCount)
                commentCount[6] = sum(commentCount[6], video.commentCount)
            }
        }

        val size = videoList.size
        viewCount[0]?.let { viewCount[0] = if (size == 0) null else it / size }
        likeCount[0]?.let { likeCount[0] = if (size == 0) null else it / size }
        dislikeCount[0]?.let { dislikeCount[0] = if (size == 0) null else it / size }
        commentCount[0]?.let { commentCount[0] = if (size == 0) null else it / size }

        vAvgVideoViewsTextView.text = viewCount[0]?.format() ?: "?"
        vViews14TextView2.text = viewCount[1]?.format() ?: "?"
        vViews30TextView2.text = viewCount[2]?.format() ?: "?"
        vViews60TextView2.text = viewCount[3]?.format() ?: "?"
        vViews90TextView2.text = viewCount[4]?.format() ?: "?"
        vViews180TextView2.text = viewCount[5]?.format() ?: "?"
        vViews365TextView2.text = viewCount[6]?.format() ?: "?"

        vAvgVideoLikesTextView.text = likeCount[0]?.format() ?: "?"
        vLikes14TextView.text = likeCount[1]?.format() ?: "?"
        vLikes30TextView.text = likeCount[2]?.format() ?: "?"
        vLikes60TextView.text = likeCount[3]?.format() ?: "?"
        vLikes90TextView.text = likeCount[4]?.format() ?: "?"
        vLikes180TextView.text = likeCount[5]?.format() ?: "?"
        vLikes365TextView.text = likeCount[6]?.format() ?: "?"

        vAvgVideoDislikesTextView.text = dislikeCount[0]?.format() ?: "?"
        vDislikes14TextView.text = dislikeCount[1]?.format() ?: "?"
        vDislikes30TextView.text = dislikeCount[2]?.format() ?: "?"
        vDislikes60TextView.text = dislikeCount[3]?.format() ?: "?"
        vDislikes90TextView.text = dislikeCount[4]?.format() ?: "?"
        vDislikes180TextView.text = dislikeCount[5]?.format() ?: "?"
        vDislikes365TextView.text = dislikeCount[6]?.format() ?: "?"

        vAvgVideoCommentsTextView.text = commentCount[0]?.format() ?: "?"
        vComments14TextView.text = commentCount[1]?.format() ?: "?"
        vComments30TextView.text = commentCount[2]?.format() ?: "?"
        vComments60TextView.text = commentCount[3]?.format() ?: "?"
        vComments90TextView.text = commentCount[4]?.format() ?: "?"
        vComments180TextView.text = commentCount[5]?.format() ?: "?"
        vComments365TextView.text = commentCount[6]?.format() ?: "?"
    }

    private fun updateGeneralStatisticsChart(generalChartTypePosition: Int? = null) {
        vGeneralStatisticsLineChart.data = dataDailyList?.let {
            if (it.isNotEmpty()) {
                val dataDailyList = ArrayList(it)
                dataDailyList.sortWith(Comparator { o1, o2 ->
                    o1.date.compareTo(o2.date)
                })
                val periodBegin = dataDailyList[0].date.minusDays(1).date()
                val chartType = generalChartTypePosition ?: vGeneralStatisticsChartTypeButtonGroup.position

                vGeneralStatisticsLineChart.xAxis.axisMinimum = 0f
                vGeneralStatisticsLineChart.xAxis.axisMaximum = dataDailyList.size + 1f

                vGeneralStatisticsLineChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return periodBegin.plusDays(value.toInt()).formatChartDate()
                    }
                }
                vGeneralStatisticsLineChart.axisLeft.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.format()}${if (chartType == 1) "%" else ""}"
                    }
                }

                val subEntries = ArrayList<Entry>()
                val viewEntries = ArrayList<Entry>()
                for (i in 0 until dataDailyList.size) {
                    val dataDaily = dataDailyList[i]
                    val x = Days.daysBetween(periodBegin, dataDaily.date).days.toFloat()

                    if (chartType == 0) {
                        subEntries.add(Entry(x, dataDaily.subs.toFloat()))
                        viewEntries.add(Entry(x, dataDaily.views.toFloat()))
                    } else {
                        val prevDataDaily = if (i == 0) dataDaily else dataDailyList[i - 1]

                        val prevSubs = prevDataDaily.subs
                        val subDif = dataDaily.subs - prevSubs
                        subEntries.add(Entry(x, if (prevSubs <= 0) 0f else subDif.toFloat() / prevSubs * 100f))

                        val prevViews = prevDataDaily.views
                        val viewDif = dataDaily.views - prevViews
                        viewEntries.add(Entry(x, if (prevViews <= 0) 0f else viewDif.toFloat() / prevViews * 100f))
                    }
                }

                val subDataSet = createDataSet(subEntries, R.string.statistics_subscribers, R.color.subs)
                val viewDataSet = createDataSet(viewEntries, R.string.statistics_views, R.color.views)

                val lineDataList = ArrayList<ILineDataSet>()
                if (vGeneralStatisticsSubsCheckBox.isChecked)
                    lineDataList.add(subDataSet)
                if (vGeneralStatisticsViewsCheckBox.isChecked)
                    lineDataList.add(viewDataSet)

                if (lineDataList.isNotEmpty())
                    LineData(lineDataList)
                else
                    null
            } else {
                null
            }
        }
        vGeneralStatisticsLineChart.notifyDataSetChanged()
        vGeneralStatisticsLineChart.invalidate()
    }

    private fun updateVideosStatisticsChart() {
        vVideosStatisticsLineChart.data = if (videoList.isNotEmpty()) {
            val videos = ArrayList(videoList)
            videos.sortWith(Comparator { o1, o2 ->
                o1.publishedAt.compareTo(o2.publishedAt)
            })
            val periodBegin = videos.first().publishedAt.minusDays(1).date()

            vVideosStatisticsLineChart.xAxis.axisMinimum = 0f
            vVideosStatisticsLineChart.xAxis.axisMaximum = Days.daysBetween(periodBegin, videos.last().publishedAt.plusDays(2).date()).days.toFloat()

            vVideosStatisticsLineChart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return periodBegin.plusDays(value.toInt()).formatChartDate()
                }
            }
            vVideosStatisticsLineChart.axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.format()
                }
            }

            vVideosStatisticsLineChart.marker = object : MarkerView(this, R.layout.view_marker) {
                override fun refreshContent(e: Entry?, highlight: Highlight?) {
                    e?.data?.let { data ->
                        if (data is Video) {
                            findViewById<TextView>(R.id.vTitleTextView).text = data.title
                            findViewById<TextView>(R.id.vPublishedAtTextView).text = data.publishedAt.toString("dd.MM.yyyy HH:mm:ss")
                            findViewById<TextView>(R.id.vDurationTextView).text = data.duration?.formatDuration() ?: "?"
                            findViewById<TextView>(R.id.vViewCountTextView).text = data.viewCount?.format() ?: "?"
                            findViewById<TextView>(R.id.vCommentCountTextView).text = data.commentCount?.format() ?: "?"
                            findViewById<TextView>(R.id.vLikeCountTextView).text = data.likeCount?.format() ?: "?"
                            findViewById<TextView>(R.id.vDislikeCountTextView).text = data.dislikeCount?.format() ?: "?"

                            val likeCount = (data.likeCount ?: 0).toFloat()
                            val dislikeCount = (data.dislikeCount ?: 0).toFloat()
                            var sum = likeCount + dislikeCount
                            if (sum == 0f)
                                sum = 1f
                            val likeView = findViewById<View>(R.id.vLikeView)
                            val dislikeView = findViewById<View>(R.id.vDislikeView)
                            val likeLayoutParams = likeView.layoutParams as LinearLayout.LayoutParams
                            val dislikeLayoutParams = dislikeView.layoutParams as LinearLayout.LayoutParams
                            likeLayoutParams.weight = likeCount / sum
                            dislikeLayoutParams.weight = dislikeCount / sum
                            likeView.layoutParams = likeLayoutParams
                            dislikeView.layoutParams = dislikeLayoutParams
                        }
                    }
                    super.refreshContent(e, highlight)
                }
            }.apply {
                chartView = vVideosStatisticsLineChart
            }

            val viewEntries = ArrayList<Entry>()
            val commentEntries = ArrayList<Entry>()
            val likeEntries = ArrayList<Entry>()
            val dislikeEntries = ArrayList<Entry>()
            for (i in 0 until videos.size) {
                val video = videos[i]
                val secondsInDay = 3600 * 24
                val days = Days.daysBetween(periodBegin, video.publishedAt).days
                val seconds = Seconds.secondsBetween(periodBegin, video.publishedAt).seconds - days * secondsInDay
                val x = days.toFloat() + seconds / secondsInDay.toFloat()

                video.viewCount?.let { viewCount ->
                    viewEntries.add(Entry(x, viewCount.toFloat(), video))
                }
                video.commentCount?.let { commentCount ->
                    commentEntries.add(Entry(x, commentCount.toFloat(), video))
                }
                video.likeCount?.let { likeCount ->
                    likeEntries.add(Entry(x, likeCount.toFloat(), video))
                }
                video.dislikeCount?.let { dislikeCount ->
                    dislikeEntries.add(Entry(x, dislikeCount.toFloat(), video))
                }
            }

            val viewDataSet = createDataSet(viewEntries, R.string.statistics_views, R.color.views)
            val commentDataSet = createDataSet(commentEntries, R.string.statistics_comments, R.color.comments)
            val likeDataSet = createDataSet(likeEntries, R.string.statistics_likes, R.color.likes)
            val dislikeDataSet = createDataSet(dislikeEntries, R.string.statistics_dislikes, R.color.dislikes)

            val lineDataList = ArrayList<ILineDataSet>()
            if (vVideosStatisticsViewsCheckBox.isChecked)
                lineDataList.add(viewDataSet)
            if (vVideosStatisticsCommentsCheckBox.isChecked)
                lineDataList.add(commentDataSet)
            if (vVideosStatisticsLikesCheckBox.isChecked)
                lineDataList.add(likeDataSet)
            if (vVideosStatisticsDislikesCheckBox.isChecked)
                lineDataList.add(dislikeDataSet)

            if (lineDataList.isNotEmpty())
                LineData(lineDataList)
            else
                null
        } else {
            null
        }
        vVideosStatisticsLineChart.notifyDataSetChanged()
        vVideosStatisticsLineChart.invalidate()
    }

    private fun createDataSet(entries: List<Entry>, labelId: Int, colorId: Int): LineDataSet {
        val dataSet = LineDataSet(entries, string(labelId))
        dataSet.color = color(colorId)
        dataSet.circleColors = listOf(color(colorId))
        dataSet.circleHoleColor = Color.WHITE
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 3f
        dataSet.setDrawCircleHole(true)
        return dataSet
    }

    fun DateTime.formatChartDate(): String {
        return this.toString("dd MMM")
    }

}
