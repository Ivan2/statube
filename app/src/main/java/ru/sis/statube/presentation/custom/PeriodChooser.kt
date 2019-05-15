package ru.sis.statube.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import org.joda.time.DateTime
import ru.sis.statube.R
import java.util.*

class PeriodChooser : FrameLayout {

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    lateinit var beginDate: DateTime
        private set
    lateinit var endDate: DateTime
        private set

    var onBeginDateChoose: ((date: DateTime) -> Unit)? = null
    var onEndDateChoose: ((date: DateTime) -> Unit)? = null

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_period_chooser, this, true)
        val beginDateTextView = view.findViewById<TextView>(R.id.vBeginDateTextView)
        val endDateTextView = view.findViewById<TextView>(R.id.vEndDateTextView)

        endDate = DateTime.now()
        beginDate = endDate.minusMonths(1)

        beginDateTextView.text = beginDate.formatPeriod()
        endDateTextView.text = endDate.formatPeriod()

        beginDateTextView.setOnClickListener { textView ->
            showDatePickerDialog(beginDate) { date ->
                beginDate = date
                beginDateTextView.text = date.formatPeriod()
                onBeginDateChoose?.let { it(date) }
            }
        }
        endDateTextView.setOnClickListener {
            showDatePickerDialog(endDate) { date ->
                endDate = date
                endDateTextView.text = date.formatPeriod()
                onEndDateChoose?.let { it(date) }
            }
        }
    }

    private fun showDatePickerDialog(currentDate: DateTime, callback: (dateTime: DateTime) -> Unit) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentDate.millis
        }
        MaterialDialog(context).show {
            datePicker(currentDate = calendar) { _, date ->
                callback(DateTime(date.timeInMillis))
            }
        }
    }

    private fun DateTime.formatPeriod(): String {
        return this.toString("dd.MM.yyyy")
    }

}