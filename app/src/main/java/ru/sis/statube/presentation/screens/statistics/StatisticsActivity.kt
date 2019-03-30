package ru.sis.statube.presentation.screens.statistics

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import ru.sis.statube.model.Channel
import ru.sis.statube.model.SocialBladeDataDaily

class StatisticsActivity : AppCompatActivity() {

    private val presenter = StatisticsPresenter()

    private var socialBladeDataDailyList: List<SocialBladeDataDaily>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val channel = intent?.getSerializableExtra(CHANNEL_DATA_KEY) as? Channel ?: return

        vTitleTextView.text = channel.title ?: ""

        vChartTypeButtonGroup.setOnPositionChangedListener { _, currentPosition, _ ->
            updateChart(currentPosition)
        }
        vSubsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateChart()
        }
        vViewsCheckBox.setOnCheckedChangeListener { _, _ ->
            updateChart()
        }


        vLineChart.isDoubleTapToZoomEnabled = true
        vLineChart.setPinchZoom(true)
        vLineChart.isScaleXEnabled = true
        vLineChart.isScaleYEnabled = false
        vLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        vLineChart.xAxis.textSize = 9f
        vLineChart.axisLeft.textSize = 9f
        vLineChart.axisRight.isEnabled = false
        vLineChart.description.isEnabled = false
        //vLineChart.setDrawMarkers(true);
        //vLineChart.getLegend().setEnabled(false);
        //vLineChart.setNoDataText(context.getString(R.string.no_data_chart));
        //vLineChart.setNoDataTextColor(chartColor);

        //vLineChart.setVisibleXRangeMaximum(10f)
        vLineChart.setMaxVisibleValueCount(10)
        /*vLineChart.getXAxis().setLabelCount(5, true);
        vLineChart.getXAxis().setAxisMaximum(5f);
        vLineChart.getXAxis().setAxisMinimum(1f);*/




        /*presenter.loadStatistics(this, channel.id) { videoList ->
            videoList?.forEach { video ->
                Log.wtf("${video.publishedAt.toString("dd.MM.yyyy")}", "${video.viewCount} ${video.likeCount} ${video.dislikeCount} ${video.commentCount}")
            }
        }*/

        presenter.loadSocialBladeStatistics(this, channel.id) { statistics ->
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
                updateChart()
            }
        }
    }

    private fun updateChart(chartTypePosition: Int? = null) {
        socialBladeDataDailyList?.let { socialBladeDataDailyList ->
            if (socialBladeDataDailyList.isNotEmpty()) {
                val dataDailyList = ArrayList(socialBladeDataDailyList)
                dataDailyList.sortWith(Comparator { o1, o2 ->
                    o1.date.compareTo(o2.date)
                })
                val maxDays = Days.daysBetween(dataDailyList[0].date, DateTime.now()).days

                vLineChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val date = DateTime.now().minusDays(maxDays - value.toInt())
                        return date.toString("dd.MM")
                    }
                }
                vLineChart.axisLeft.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "$value%"
                    }
                }

                val subEntries = ArrayList<Entry>()
                val viewEntries = ArrayList<Entry>()
                val chartType = chartTypePosition ?: vChartTypeButtonGroup.position
                for (i in 0 until dataDailyList.size) {
                    val dataDaily = dataDailyList[i]
                    if (chartType == 0) {
                        subEntries.add(Entry(i.toFloat(), dataDaily.subs.toFloat()))
                        viewEntries.add(Entry(i.toFloat(), dataDaily.views.toFloat()))
                    } else {
                        val prevDataDaily = if (i == 0) dataDaily else dataDailyList[i - 1]
                        val subDif = dataDaily.subs - prevDataDaily.subs
                        subEntries.add(Entry(i.toFloat(), subDif.toFloat() / prevDataDaily.subs * 100f))
                        val viewDif = dataDaily.views - prevDataDaily.views
                        viewEntries.add(Entry(i.toFloat(), viewDif.toFloat() / prevDataDaily.views * 100f))
                    }
                }

                val subDataSet = LineDataSet(subEntries, "Подписки")
                subDataSet.color = Color.RED
                subDataSet.circleColors = listOf(Color.RED)
                subDataSet.circleHoleColor = Color.WHITE
                subDataSet.lineWidth = 2f
                subDataSet.circleRadius = 4f
                subDataSet.setDrawCircleHole(true)

                val viewDataSet = LineDataSet(viewEntries, "Просмотры")
                viewDataSet.color = Color.BLUE
                viewDataSet.circleColors = listOf(Color.BLUE)
                viewDataSet.circleHoleColor = Color.WHITE
                viewDataSet.lineWidth = 2f
                viewDataSet.circleRadius = 4f
                viewDataSet.setDrawCircleHole(true)

                vLineChart.data =
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

                vLineChart.invalidate()
            }
        }
    }

}
