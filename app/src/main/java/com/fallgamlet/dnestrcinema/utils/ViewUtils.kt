package com.fallgamlet.dnestrcinema.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import com.fallgamlet.dnestrcinema.R


object ViewUtils {

    fun hideKeyboard(activity: Activity?, v: View?) {
        var v = v
        if (activity == null) {
            return
        }

        if (v == null) {
            v = activity.window.currentFocus
        }

        if (v == null) {
            return
        }

        hideKeyboard(activity as Context?, v)
    }

    fun hideKeyboard(context: Context?, view: View?) {
        if (context == null || view == null) {
            return
        }

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(activity: Activity?, v: View?) {
        var v = v
        if (activity == null) {
            return
        }

        if (v == null) {
            v = activity.window.currentFocus
        }

        if (v == null) {
            return
        }

        showKeyboard(activity, v)
    }

    fun showKeyboard(context: Context?, view: View?) {
        if (context == null || view == null) {
            return
        }

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT)
                .show()
    }

    fun showSnackbar(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show()
    }

    fun shareApp(context: Context) {
        val builder = StringBuilder()
        builder.append(context.getString(R.string.app_name))
        builder.append(" \n ")
        builder.append("https://play.google.com/store/apps/details?id=")
        builder.append(context.getString(R.string.app_id))

        val message = builder.toString()

        share(context, message)
    }

    fun share(context: Context, message: String) {
        try {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.type = "text/plain"
            context.startActivity(Intent.createChooser(sendIntent, "Поделиться"))
        } catch (e: Exception) {
            LogUtils.log("Share", "Fail then sharing", e)
        }

    }

    @JvmOverloads
    fun setVisible(view: View, visible: Boolean, invisibleState: Int = View.GONE) {
        view.visibility = if (visible) View.VISIBLE else invisibleState
    }

}
