package ru.sis.statube.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.apptik.widget.MultiSlider
import ru.sis.statube.R
import kotlin.math.min

open class Range : FrameLayout {

    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(attrs) }

    var onValueChanged: ((min: Long, max: Long) -> String)? = null

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_range, this, true)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Range, 0, 0)
            try {
                val text = typedArray.getString(R.styleable.Range_r_text)
                val drawableResId = typedArray.getResourceId(R.styleable.Range_r_drawable, -1)

                text?.let {
                    findViewById<TextView>(R.id.vTextView).text = text
                }
                if (drawableResId != -1)
                    findViewById<ImageView>(R.id.vImageView).setImageResource(drawableResId)
            } catch (_: Exception) {
            } finally {
                typedArray.recycle()
            }
        }
    }

    fun setMinMax(minValue: Long, maxValue: Long) {
        val textView = findViewById<TextView>(R.id.vTextView)
        val slider = findViewById<MultiSlider>(R.id.vMultiSlider)

        var thumb1Value = 0
        var thumb2Value = min(minValue, 10000L).toInt()

        slider.apply {
            min = thumb1Value
            max = thumb2Value
            step = 1
            setOnThumbValueChangeListener { _, _, thumbIndex, value ->
                when (thumbIndex) {
                    0 -> thumb1Value = value
                    1 -> thumb2Value = value
                }
                val value1Float = thumb1Value.toFloat() / max
                val value2Float = thumb2Value.toFloat() / max
                val diff = maxValue - minValue
                val countFrom = minValue + (diff * value1Float).toInt()
                val countTo = minValue + (diff * value2Float).toInt()

                textView.text = onValueChanged?.let { it(countFrom, countTo) }
            }
            getThumb(0).value = thumb1Value
            getThumb(1).value = thumb2Value
        }
    }

}