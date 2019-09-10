package ru.sis.statube.presentation.screens.statistics.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

open class CustomHighlightLineChart : LineChart {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private val paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
    }

    override fun drawMarkers(canvas: Canvas?) {
        if (valuesToHighlight()) {
            mIndicesToHighlight.forEach { highlight ->
                val pos = getMarkerPosition(highlight)
                val x = pos[0]
                val y = pos[1]
                if (mViewPortHandler.isInBounds(x, y)) {
                    val set = mData.getDataSetByIndex(highlight.dataSetIndex)
                    val radius = set.circleRadius * 2
                    paint.color = set.color
                    canvas?.drawOval(x - radius, y - radius, x + radius, y + radius, paint)
                }
            }
        }
        super.drawMarkers(canvas)
    }

    override fun setData(data: LineData?) {
        super.setData(data)
        data?.dataSets?.forEach { dataSet ->
            if (dataSet is LineDataSet)
                dataSet.setDrawHighlightIndicators(false)
        }
    }

}