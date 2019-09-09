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
    var onValueChangeEnded: ((min: Long, max: Long) -> Unit)? = null

    private var minValue: Long = 0
    private var maxValue: Long = 0

    private var thumb1Value: Int = 0
    private var thumb2Value: Int = 0

    private var countFrom: Long = 0
    private var countTo: Long = 0

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
        this.minValue = minValue
        this.maxValue = maxValue

        val textView = findViewById<TextView>(R.id.vTextView)
        val slider = findViewById<MultiSlider>(R.id.vMultiSlider)

        thumb1Value = 0
        thumb2Value = min(maxValue - minValue, 10000L).toInt()

        slider.apply {
            min = thumb1Value
            max = thumb2Value
            step = 1

            getThumb(0).value = thumb1Value
            getThumb(1).value = thumb2Value

            setOnTrackingChangeListener(object : MultiSlider.OnTrackingChangeListener {
                override fun onStartTrackingTouch(multiSlider: MultiSlider?, thumb: MultiSlider.Thumb?, value: Int) {}
                override fun onStopTrackingTouch(multiSlider: MultiSlider?, thumb: MultiSlider.Thumb?, value: Int) {
                    calcCount(slider)
                    updateTextView(textView)
                    onValueChangeEnded?.let { it(countFrom, countTo) }
                }
            })

            setOnThumbValueChangeListener { _, _, thumbIndex, value ->
                when (thumbIndex) {
                    0 -> thumb1Value = value
                    1 -> thumb2Value = value
                }
                calcCount(slider)
                updateTextView(textView)
            }

            calcCount(slider)
            updateTextView(textView)
            onValueChangeEnded?.let { it(countFrom, countTo) }
        }
    }

    private fun calcCount(slider: MultiSlider) {
        val value1Float = thumb1Value.toFloat() / slider.max
        val value2Float = thumb2Value.toFloat() / slider.max
        val diff = maxValue - minValue
        countFrom = minValue + (diff * value1Float).toInt()
        countTo = minValue + (diff * value2Float).toInt()
    }

    private fun updateTextView(textView: TextView) {
        textView.text = onValueChanged?.let { it(countFrom, countTo) } ?: ""
    }

}