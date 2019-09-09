package ru.sis.statube.presentation.presenter

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import ru.sis.statube.R

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

}
