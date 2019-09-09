package ru.sis.statube.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class ExpandableTextView : AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val collapseLineCount = 14
    private val expandLineCount = 1000000

    var isExpanded = false
        private set

    var collapseExpandView: View? = null
        set(value) {
            field = value
            value?.let {
                requestLayout()
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        maxLines = expandLineCount
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        collapseExpandView?.let { collapseExpandView ->
            if (lineCount <= collapseLineCount) {
                if (collapseExpandView.visibility != View.GONE)
                    collapseExpandView.visibility = View.GONE
            } else {
                if (collapseExpandView.visibility != View.VISIBLE)
                    collapseExpandView.visibility = View.VISIBLE
            }
        }
        if (!isExpanded) {
            maxLines = collapseLineCount
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun toggle() {
        isExpanded = !isExpanded
        requestLayout()
    }

}