package com.fallgamlet.dnestrcinema.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.utils.ViewUtils
import com.google.android.material.snackbar.Snackbar

fun Activity.showError(throwable: Throwable) {
    findViewById<View>(android.R.id.content)?.showError(throwable)
}

fun Fragment.showError(throwable: Throwable) {
    view?.showError(throwable)
}

fun View.showError(throwable: Throwable) {
    val message = throwable.toString()

    ViewUtils.makeSnackbar(this, message, Snackbar.LENGTH_LONG)
        .setAction(R.string.label_more) { context?.showErrorDetails(throwable) }
        .show()
}

fun Context.showErrorDetails(throwable: Throwable) {
    val message = "${throwable.message}\n\n$throwable"

    AlertDialog.Builder(this)
        .setMessage(message)
        .create()
        .show()
}
