package ru.sis.statube.presentation.screens.statistics.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import com.github.mikephil.charting.components.MarkerView
import ru.sis.statube.R

@SuppressLint("ViewConstructor")
open class ClickableMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    var touchableViewRect: Rect? = null

    private val touchableMarkerView: View = findViewById<View>(R.id.vTouchableMarkerView)

    init {
        touchableMarkerView.isClickable = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchableMarkerView.dispatchTouchEvent(event)
        return true
    }

    override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        val left = (posX + offset.x + touchableMarkerView.x).toInt()
        val top = (posY + offset.y + touchableMarkerView.y).toInt()
        touchableViewRect = Rect(left, top, left + touchableMarkerView.measuredWidth,
            top + touchableMarkerView.measuredHeight)
    }

}