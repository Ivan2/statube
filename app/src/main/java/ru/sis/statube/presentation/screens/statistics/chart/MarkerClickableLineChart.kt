package ru.sis.statube.presentation.screens.statistics.chart

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.github.mikephil.charting.charts.LineChart

class MarkerClickableLineChart : LineChart {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var isHandled = false
        if (isDrawMarkersEnabled && valuesToHighlight() && event != null) {
            (this.marker as? ClickableMarkerView)?.let { marker ->
                marker.touchableViewRect?.let { rect ->
                    if (rect.contains(event.x.toInt(), event.y.toInt())) {
                        val changedEvent = MotionEvent.obtain(event.downTime, event.eventTime,
                            event.action, event.x - rect.left, event.y - rect.top, event.metaState)
                        changedEvent.recycle()
                        marker.onTouchEvent(changedEvent)
                        isHandled = true
                    }
                }
            }
        }
        return if (isHandled) true else super.onTouchEvent(event)
    }

}