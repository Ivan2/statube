package ru.sis.statube.presentation.screens.statistics.chart

import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.MPPointF
import kotlin.math.pow
import kotlin.math.sqrt

class NearestPointHighlighter(chart: BarLineScatterCandleBubbleDataProvider?) : ChartHighlighter<BarLineScatterCandleBubbleDataProvider>(chart) {

    override fun getHighlight(x: Float, y: Float): Highlight? {
        var minDist = -1.0
        var nearestPoint: MPPointD? = null
        mChart.data.dataSets.forEach { dataSet ->
            for (i in 0 until dataSet.entryCount) {
                val entry = dataSet.getEntryForIndex(i)
                val pixels = mChart.getTransformer(dataSet.axisDependency).getPixelForValues(entry.x, entry.y)
                val dist = sqrt((x - pixels.x).pow(2) + (y - pixels.y).pow(2))
                if (minDist < 0 || dist < minDist) {
                    minDist = dist
                    nearestPoint = pixels
                }
            }
        }

        val pos2 = nearestPoint?.let {
            MPPointF(it.x.toFloat(), it.y.toFloat())
        } ?: MPPointF(x, y)

        val pos = getValsForTouch(pos2.x, pos2.y)
        val xVal = pos.x.toFloat()
        MPPointD.recycleInstance(pos)
        return getHighlightForX(xVal, x, y)
    }

}