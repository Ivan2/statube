package ru.sis.statube.presentation.screens.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_statistics.*
import org.joda.time.DateTime
import org.joda.time.Days
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
    private var videoList: List<Video>? = null

    private lateinit var beginDate: DateTime
    private lateinit var endDate: DateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        vBackButton.setOnClickListener {
            onBackPressed()
        }

        channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        vGeneralStatisticsRefreshButton.visibility = View.VISIBLE
        vGeneralStatisticsLoadingProgressBar.visibility = View.INVISIBLE
        updateGeneralStatisticsLastUpdatedDateTime()
        vGeneralStatisticsRefreshButton.setOnClickListener {
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


        vVideosStatisticsRefreshButton.visibility = View.VISIBLE
        vVideosStatisticsLoadingProgressBar.visibility = View.INVISIBLE
        updateVideosStatisticsLastUpdatedDateTime()
        vVideosStatisticsRefreshButton.setOnClickListener {
            MaterialDialog(this).show {
                message(R.string.statistics_dialog_long_loading_message)
                negativeButton(R.string.statistics_dialog_long_loading_cancel)
                positiveButton(R.string.statistics_dialog_long_loading_continue) {
                    updateVideosStatistics()
                }
            }
        }

        endDate = DateTime.now()
        beginDate = endDate.minusMonths(2)

        updateBeginDateText()
        updateEndDateText()

        vVideosStatisticsBeginDateLayout.setOnClickListener {
            showDatePickerDialog(beginDate) { date ->
                beginDate = date
                updateBeginDateText()
                updateVideosStatisticsLocal()
                //updateVideosStatisticsChart()
            }
        }
        vVideosStatisticsEndDateLayout.setOnClickListener {
            showDatePickerDialog(endDate) { date ->
                endDate = date
                updateEndDateText()
                updateVideosStatisticsLocal()
                //updateVideosStatisticsChart()
            }
        }
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
        vVideosStatisticsBeginDateTextView.text = beginDate.formatPeriod()
    }

    private fun updateEndDateText() {
        vVideosStatisticsEndDateTextView.text = endDate.formatPeriod()
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
        vGeneralStatisticsUpdatedTextView.text = "?"
        presenter.loadGeneralStatisticsLastUpdatedDateTime(channel.id) { statisticsLastUpdated ->
            vGeneralStatisticsUpdatedTextView.text = statisticsLastUpdated?.date?.formatUpdate() ?: "?"
        }
    }

    private fun updateVideosStatisticsLastUpdatedDateTime() {
        vVideosStatisticsUpdatedTextView.text = "?"
        vVideosStatisticsDatesTextView.text = string(R.string.statistics_dates_template, "?", "?")
        channel.uploads?.let { uploads ->
            presenter.loadVideosStatisticsLastUpdatedDateTime(uploads) { statisticsLastUpdated ->
                vVideosStatisticsUpdatedTextView.text = statisticsLastUpdated?.date?.formatUpdate() ?: "?"
                vVideosStatisticsDatesTextView.text = string(R.string.statistics_dates_template,
                    statisticsLastUpdated?.beginDate?.formatPeriod() ?: "?",
                    statisticsLastUpdated?.endDate?.formatPeriod() ?: "?")
            }
        }
    }

    private fun updateGeneralStatistics() {
        vGeneralStatisticsRefreshButton.visibility = View.INVISIBLE
        vGeneralStatisticsLoadingProgressBar.visibility = View.VISIBLE

        presenter.loadGeneralStatistics(this, channel.id, { statistics ->
            updateGeneralStatistics(statistics)
            vGeneralStatisticsRefreshButton.visibility = View.VISIBLE
            vGeneralStatisticsLoadingProgressBar.visibility = View.INVISIBLE
            updateGeneralStatisticsLastUpdatedDateTime()
        }, {
            vGeneralStatisticsRefreshButton.visibility = View.VISIBLE
            vGeneralStatisticsLoadingProgressBar.visibility = View.INVISIBLE
        })
    }

    private fun updateGeneralStatisticsLocal() {
        vGeneralStatisticsRefreshButton.visibility = View.INVISIBLE
        vGeneralStatisticsLoadingProgressBar.visibility = View.VISIBLE

        presenter.loadGeneralStatisticsLocal(channel.id) { statistics ->
            updateGeneralStatistics(statistics)
            vGeneralStatisticsRefreshButton.visibility = View.VISIBLE
            vGeneralStatisticsLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun updateGeneralStatistics(statistics: GeneralStatistics?) {
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
        updateGeneralStatisticsChart()
    }

    private fun updateVideosStatisticsLocal() {
        vVideosStatisticsRefreshButton.visibility = View.INVISIBLE
        vVideosStatisticsLoadingProgressBar.visibility = View.VISIBLE

        presenter.loadVideosStatisticsLocal(channel.id, beginDate, endDate) { videoList ->
            this.videoList = videoList
            updateVideosStatisticsChart()
            vVideosStatisticsRefreshButton.visibility = View.VISIBLE
            vVideosStatisticsLoadingProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun updateVideosStatistics() {
        channel.uploads?.let { uploads ->
            vVideosStatisticsRefreshButton.visibility = View.INVISIBLE
            vVideosStatisticsLoadingProgressBar.visibility = View.VISIBLE

            presenter.loadVideosStatistics(this, uploads, beginDate, endDate, channel.id) { videoList ->
                this.videoList = videoList
                updateVideosStatisticsChart()
                vVideosStatisticsRefreshButton.visibility = View.VISIBLE
                vVideosStatisticsLoadingProgressBar.visibility = View.INVISIBLE
                updateVideosStatisticsLastUpdatedDateTime()
            }
        }
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
        vVideosStatisticsLineChart.data = videoList?.let { videoList ->
            if (videoList.isNotEmpty()) {
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

                val viewEntries = ArrayList<Entry>()
                val commentEntries = ArrayList<Entry>()
                val likeEntries = ArrayList<Entry>()
                val dislikeEntries = ArrayList<Entry>()
                for (i in 0 until videos.size) {
                    val video = videos[i]
                    val x = Days.daysBetween(periodBegin, video.publishedAt).days.toFloat()

                    video.viewCount?.let { viewCount ->
                        viewEntries.add(Entry(x, viewCount.toFloat()))
                    }
                    video.commentCount?.let { commentCount ->
                        commentEntries.add(Entry(x, commentCount.toFloat()))
                    }
                    video.likeCount?.let { likeCount ->
                        likeEntries.add(Entry(x, likeCount.toFloat()))
                    }
                    video.dislikeCount?.let { dislikeCount ->
                        dislikeEntries.add(Entry(x, dislikeCount.toFloat()))
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

}
