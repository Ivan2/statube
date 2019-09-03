package ru.sis.statube.presentation.custom

import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.omjoonkim.skeletonloadingview.SkeletonLoadingView
import ru.sis.statube.additional.inflate

class SkeletonViewController(
    private val swipeRefreshLayout: SwipeRefreshLayout?,
    private val contentView: View,
    private val emptyView: View,
    private val skeletonContainer: ViewGroup
) {

    enum class State {
        CONTENT,
        EMPTY,
        SKELETON
    }

    var state: State = State.EMPTY
        set(value) {
            field = value
            when (value) {
                State.CONTENT -> {
                    swipeRefreshLayout?.visibility = View.VISIBLE
                    contentView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                    skeletonContainer.visibility = View.GONE
                    changeSkeletonState(skeletonContainer, false)
                }
                State.EMPTY -> {
                    swipeRefreshLayout?.visibility = View.VISIBLE
                    contentView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                    skeletonContainer.visibility = View.GONE
                    changeSkeletonState(skeletonContainer, false)
                }
                State.SKELETON -> {
                    swipeRefreshLayout?.visibility = View.GONE
                    contentView.visibility = View.GONE
                    emptyView.visibility = View.GONE
                    skeletonContainer.visibility = View.VISIBLE
                    changeSkeletonState(skeletonContainer, true)
                }
            }

        }

    var onRefreshListener: (() -> Unit)? = null

    init {
        swipeRefreshLayout?.setOnRefreshListener {
            onRefreshListener?.invoke()
        }

        state = State.EMPTY
    }

    fun inflateSkeleton(resId: Int, count: Int) {
        skeletonContainer.removeAllViews()
        for (i in 1..count)
            skeletonContainer.inflate(resId)
    }

    private fun changeSkeletonState(view: View, start: Boolean) {
        when (view) {
            is ViewGroup -> {
                for (i in 0 until view.childCount)
                    changeSkeletonState(view.getChildAt(i), start)
            }
            is SkeletonLoadingView -> {
                if (start)
                    view.start()
                else
                    view.stop()
            }
        }
    }

}