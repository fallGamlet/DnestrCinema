package com.fallgamlet.dnestrcinema.ui.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.utils.ViewUtils
import com.google.android.material.snackbar.Snackbar

fun Fragment.showError(throwable: Throwable) {
    val view = this.view?: return

    val message = throwable.toString()

    ViewUtils.makeSnackbar(view, message, Snackbar.LENGTH_LONG)
        .setAction(R.string.label_more) { showErrorDetails(throwable) }
        .show()
}

fun Fragment.showErrorDetails(throwable: Throwable) {
    val context = this.context ?: return
    val message = "${throwable.message}\n\n$throwable"

    AlertDialog.Builder(context)
        .setMessage(message)
        .create()
        .show()
}
