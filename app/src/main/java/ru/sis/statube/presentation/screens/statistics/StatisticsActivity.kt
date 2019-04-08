package ru.sis.statube.presentation.screens.statistics

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_statistics.*
import org.joda.time.DateTime
import org.joda.time.Days
import ru.sis.statube.R
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.additional.color
import ru.sis.statube.additional.date
import ru.sis.statube.model.Channel
import ru.sis.statube.model.SocialBladeDataDaily
import ru.sis.statube.model.SocialBladeStatistics
import ru.sis.statube.model.Video
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class StatisticsActivity : AppCompatActivity() {

    private val presenter = StatisticsPresenter()

    private lateinit var channel: Channel

    private var socialBladeDataDailyList: List<SocialBladeDataDaily>? = null
    private var videoList: List<Video>? = null

    private lateinit var beginDate: DateTime
    private lateinit var endDate: DateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
//TODO vertical lines on actual data
        channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        vRefresh1Button.visibility = View.VISIBLE
        vLoading1ProgressBar.visibility = View.INVISIBLE
        vUpdated1TextView.text = presenter.getStatistics1LastUpdatedDateTime(this)?.formatUpdate() ?: "?"
        vRefresh1Button.setOnClickListener {
            updateSocialBladeStatistics()
        }

        vChartTypeButtonGroup.setOnPositionChangedListener { _, currentPosition, _ ->
            updateChart1(currentPosition)
        }
        vSubsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateChart1()
        }
        vViewsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateChart1()
        }


        vRefresh2Button.visibility = View.VISIBLE
        vLoading2ProgressBar.visibility = View.INVISIBLE
        vUpdated2TextView.text = presenter.getStatistics2LastUpdatedDateTime(this)?.formatUpdate() ?: "?"
        vRefresh2Button.setOnClickListener {
            MaterialDialog(this).show {
                message(text = "Загрузка данных может быть долгой")
                negativeButton(text = "Отмена")
                positiveButton(text = "Продолжить") {
                    updateVideosStatistics()
                }
            }
        }

        endDate = DateTime.now()
        beginDate = endDate.minusMonths(1)

        updateBeginDateText()
        updateEndDateText()

        vBeginDateLayout.setOnClickListener {
            showDatePickerDialog(beginDate) { date ->
                beginDate = date
                updateBeginDateText()
                updateChart2()
            }
        }
        vEndDateLayout.setOnClickListener {
            showDatePickerDialog(endDate) { date ->
                endDate = date
                updateEndDateText()
                updateChart2()
            }
        }

        prepareChart(vLineChart)
        prepareChart(vLineChart2)

        if (channel.uploads.isNullOrEmpty()) {
            vVideoStatisticsLayout.visibility = View.GONE
        }

        updateSocialBladeLocalStatistics()
    }

    private fun showDatePickerDialog(currentDate: DateTime, callback: (dateTime: DateTime) -> Unit) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentDate.millis
        }
        MaterialDialog(this).show {
            datePicker(currentDate = calendar) { dialog, date ->
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
        chart.setNoDataText("Нет данных")
        chart.setNoDataTextColor(Color.RED)
        chart.setMaxVisibleValueCount(14)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.data = null
    }

    private fun updateSocialBladeStatistics() {
        vRefresh1Button.visibility = View.INVISIBLE
        vLoading1ProgressBar.visibility = View.VISIBLE

        presenter.loadSocialBladeStatistics(this, channel.id, { statistics ->
            updateSocialBladeLocalStatistics(statistics)
            vRefresh1Button.visibility = View.VISIBLE
            vLoading1ProgressBar.visibility = View.INVISIBLE
            val now = DateTime.now()
            presenter.setStatistics1LastUpdatedDateTime(this, now)
            vUpdated1TextView.text = now.formatUpdate()
        }, {
            vRefresh1Button.visibility = View.VISIBLE
            vLoading1ProgressBar.visibility = View.INVISIBLE
        })
    }

    private fun updateSocialBladeLocalStatistics() {
        vRefresh1Button.visibility = View.INVISIBLE
        vLoading1ProgressBar.visibility = View.VISIBLE

        presenter.loadSocialBladeLocalStatistics(channel.id) { statistics ->
            updateSocialBladeLocalStatistics(statistics)
            vRefresh1Button.visibility = View.VISIBLE
            vLoading1ProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun updateSocialBladeLocalStatistics(statistics: SocialBladeStatistics?) {
        vAvgDailySubsTextView.text = statistics?.avgDailySubs?.toString() ?: "?"
        vAvgDailyViewsTextView.text = statistics?.avgDailyViews?.toString() ?: "?"
        vSubs14TextView.text = statistics?.subsByPeriod?.by14?.toString() ?: "?"
        vViews14TextView.text = statistics?.viewsByPeriod?.by14?.toString() ?: "?"
        vSubs30TextView.text = statistics?.subsByPeriod?.by30?.toString() ?: "?"
        vViews30TextView.text = statistics?.viewsByPeriod?.by30?.toString() ?: "?"
        vSubs60TextView.text = statistics?.subsByPeriod?.by60?.toString() ?: "?"
        vViews60TextView.text = statistics?.viewsByPeriod?.by60?.toString() ?: "?"
        vSubs90TextView.text = statistics?.subsByPeriod?.by90?.toString() ?: "?"
        vViews90TextView.text = statistics?.viewsByPeriod?.by90?.toString() ?: "?"
        vSubs180TextView.text = statistics?.subsByPeriod?.by180?.toString() ?: "?"
        vViews180TextView.text = statistics?.viewsByPeriod?.by180?.toString() ?: "?"
        vSubs365TextView.text = statistics?.subsByPeriod?.by365?.toString() ?: "?"
        vViews365TextView.text = statistics?.viewsByPeriod?.by365?.toString() ?: "?"

        statistics?.dataDailyList?.let { dataDailyList ->
            socialBladeDataDailyList = dataDailyList
        }
        updateChart1()
    }

    private fun updateVideosStatistics() {
        channel.uploads?.let { uploads ->
            vRefresh2Button.visibility = View.INVISIBLE
            vLoading2ProgressBar.visibility = View.VISIBLE

            presenter.loadStatistics(this, uploads, beginDate, endDate) { videoList ->
                this.videoList = videoList
                updateChart2()
                vRefresh2Button.visibility = View.VISIBLE
                vLoading2ProgressBar.visibility = View.INVISIBLE
                val now = DateTime.now()
                presenter.setStatistics2LastUpdatedDateTime(this, now)
                vUpdated2TextView.text = now.formatUpdate()
            }

        }
    }

    private fun Float.format(): String {
        val df = NumberFormat.getNumberInstance(Locale.ENGLISH) as DecimalFormat
        df.applyPattern("###,###.#")
        return df.format(this)
    }

    private fun updateChart1(chartTypePosition: Int? = null) {
        vLineChart.data = socialBladeDataDailyList?.let { socialBladeDataDailyList ->
            if (socialBladeDataDailyList.isNotEmpty()) {
                val dataDailyList = ArrayList(socialBladeDataDailyList)
                dataDailyList.sortWith(Comparator { o1, o2 ->
                    o1.date.compareTo(o2.date)
                })
                val periodBegin = dataDailyList[0].date.minusDays(1).date()
                val chartType = chartTypePosition ?: vChartTypeButtonGroup.position

                vLineChart.xAxis.axisMinimum = 0f
                vLineChart.xAxis.axisMaximum = dataDailyList.size + 1f

                vLineChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return periodBegin.plusDays(value.toInt()).formatChartDate()
                    }
                }
                vLineChart.axisLeft.valueFormatter = object : ValueFormatter() {
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
                        subEntries.add(Entry(x, if (prevSubs <= 0) 0f else subDif / prevSubs * 100f))

                        val prevViews = prevDataDaily.views
                        val viewDif = dataDaily.views - prevViews
                        viewEntries.add(Entry(x, if (prevViews <= 0) 0f else viewDif / prevViews * 100f))
                    }
                }

                val subDataSet = createDataSet(subEntries, "Подписки", R.color.subs)
                val viewDataSet = createDataSet(viewEntries, "Просмотры", R.color.views)

                if (vSubsCheckBox.isChecked && vViewsCheckBox.isChecked) {
                    LineData(subDataSet, viewDataSet)
                } else {
                    if (vSubsCheckBox.isChecked) {
                        LineData(subDataSet)
                    } else {
                        if (vViewsCheckBox.isChecked) {
                            LineData(viewDataSet)
                        } else {
                            null
                        }
                    }
                }
            } else {
                null
            }
        }
        vLineChart.notifyDataSetChanged()
        vLineChart.invalidate()
    }

    private fun updateChart2() {
        vLineChart2.data = videoList?.let { videoList ->
            if (videoList.isNotEmpty()) {
                val videos = ArrayList(videoList)
                videos.sortWith(Comparator { o1, o2 ->
                    o1.publishedAt.compareTo(o2.publishedAt)
                })
                val periodBegin = videos.first().publishedAt.minusDays(1).date()

                vLineChart2.xAxis.axisMinimum = 0f
                vLineChart2.xAxis.axisMaximum = Days.daysBetween(periodBegin, videos.last().publishedAt.plusDays(3).date()).days.toFloat()

                vLineChart2.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return periodBegin.plusDays(value.toInt()).formatChartDate()
                    }
                }
                vLineChart2.axisLeft.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.format()
                    }
                }

                val likeEntries = ArrayList<Entry>()
                val viewEntries = ArrayList<Entry>()
                for (i in 0 until videos.size) {
                    val video = videos[i]
                    val x = Days.daysBetween(periodBegin, video.publishedAt).days.toFloat()

                    video.viewCount?.let { viewCount ->
                        viewEntries.add(Entry(x, viewCount.toFloat()))
                    }
                    video.likeCount?.let { likeCount ->
                        likeEntries.add(Entry(x, likeCount.toFloat()))
                    }
                }

                val likeDataSet = createDataSet(likeEntries, "Лайки", R.color.subs)
                val viewDataSet = createDataSet(viewEntries, "Просмотры", R.color.views)

                /*if (vSubsCheckBox.isChecked && vViewsCheckBox.isChecked) {
                    LineData(subDataSet, viewDataSet)
                } else {
                    if (vSubsCheckBox.isChecked) {
                        LineData(subDataSet)
                    } else {
                        if (vViewsCheckBox.isChecked) {*/
                            LineData(likeDataSet, viewDataSet)
                        /*} else {
                            null
                        }
                    }
                }*/
            } else {
                null
            }
        }
        vLineChart2.invalidate()
    }

    private fun createDataSet(entries: List<Entry>, label: String, colorId: Int): LineDataSet {
        val dataSet = LineDataSet(entries, label)
        dataSet.color = color(colorId)
        dataSet.circleColors = listOf(color(colorId))
        dataSet.circleHoleColor = Color.WHITE
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(true)
        return dataSet
    }

}
