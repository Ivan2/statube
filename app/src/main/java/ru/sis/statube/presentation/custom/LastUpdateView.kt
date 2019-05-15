package ru.sis.statube.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import org.joda.time.DateTime
import ru.sis.statube.R
import ru.sis.statube.additional.formatPeriod
import ru.sis.statube.additional.formatUpdate
import ru.sis.statube.additional.string

class LastUpdateView : FrameLayout {

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    var onUpdateClickListener: (() -> Unit)? = null

    var isPeriodVisible = true
        set(value) {
            field = value
            findViewById<View>(R.id.vDatesTextView).visibility = if (value) View.VISIBLE else View.GONE
        }

    var isLoading = false
        set(value) {
            field = value
            val refreshButton = findViewById<View>(R.id.vRefreshButton)
            val loadingProgressBar = findViewById<View>(R.id.vLoadingProgressBar)
            if (value) {
                refreshButton.visibility = View.INVISIBLE
                loadingProgressBar.visibility = View.VISIBLE
            } else {
                refreshButton.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.INVISIBLE
            }
        }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_last_update, this, true)

        view.findViewById<View>(R.id.vRefreshButton).setOnClickListener {
            onUpdateClickListener?.let { it() }
        }

        isPeriodVisible = true
        isLoading = false
        update(null, null, null)
    }

    fun update(updatedDateTime: DateTime?, beginDate: DateTime?, endDate: DateTime?) {
        findViewById<TextView>(R.id.vUpdatedTextView).text = updatedDateTime?.formatUpdate() ?: "?"
        findViewById<TextView>(R.id.vDatesTextView).text = context.string(R.string.period_template,
            beginDate?.formatPeriod() ?: "?", endDate?.formatPeriod() ?: "?")
    }

    fun update(updatedDateTime: DateTime?) = update(updatedDateTime, null, null)

}