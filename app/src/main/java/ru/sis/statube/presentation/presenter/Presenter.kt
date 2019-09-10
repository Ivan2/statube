package ru.sis.statube.presentation.presenter

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import ru.sis.statube.R
import ru.sis.statube.additional.string
import ru.sis.statube.exception.ResponseException

open class Presenter {

    private var progressDialog: MaterialDialog? = null

    protected fun showProgressDialog(context: Context) {
        progressDialog = MaterialDialog(context).show {
            customView(R.layout.dialog_progress)
            cancelable(false)
        }
    }

    protected fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    protected fun onError(context: Context, e: Exception) {
        if (e is ResponseException) {
            MaterialDialog(context).show {
                title(text = context.string(R.string.error_title_template, e.errorCode?.let { " $it" } ?: ""))
                message(text = e.errorMessage ?: context.string(R.string.unknown_error))
                positiveButton(R.string.ok)
            }
        }
    }

}
