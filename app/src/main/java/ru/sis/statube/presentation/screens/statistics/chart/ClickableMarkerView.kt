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
        var newPosX = posX
        var newPosY = posY

        if (newPosX > chartView.measuredWidth / 2)
            newPosX -= this.measuredWidth
        if (newPosX < 0)
            newPosX = 0f

        if (newPosY > chartView.measuredHeight / 2)
            newPosY -= this.measuredHeight
        if (newPosY < 0)
            newPosY = 0f

        super.draw(canvas, newPosX, newPosY)

        val offset = getOffsetForDrawingAtPoint(newPosX, newPosY)
        val left = (newPosX + offset.x + touchableMarkerView.x).toInt()
        val top = (newPosY + offset.y + touchableMarkerView.y).toInt()
        touchableViewRect = Rect(left, top, left + touchableMarkerView.measuredWidth,
            top + touchableMarkerView.measuredHeight)
    }

}