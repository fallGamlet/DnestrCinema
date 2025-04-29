package com.fallgamlet.dnestrcinema.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.fallgamlet.dnestrcinema.R
import com.google.android.material.snackbar.Snackbar


object ViewUtils {

    @JvmOverloads
    fun hideKeyboard(activity: Activity?, v: View? = null) {
        val view = v
            ?: activity?.window?.currentFocus
            ?: activity?.window?.decorView

        hideKeyboard(activity as Context?, view)
    }

    fun hideKeyboard(context: Context?, view: View?) {
        context ?: return
        val v = view ?: View(context)

        try {
            v.postDelayed({
                val binder = v.windowToken
                hideKeyboard(context, binder)
            }, 1)
        } catch (ignored: Exception) { }
    }

    private fun hideKeyboard(context: Context, binder: IBinder?) {
        binder ?: return
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS)
        imm?.hideSoftInputFromInputMethod(binder, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    fun showKeyboard(activity: Activity?, view: View?) {
        val v = view
            ?: activity?.window?.currentFocus
            ?: return

        showKeyboard(activity as? Context, v)
    }

    fun showKeyboard(context: Context?, view: View?) {
        view ?: return
        context ?: return

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    fun showToast(context: Context?, text: CharSequence) {
        context ?: return
        Toast.makeText(context, text, Toast.LENGTH_SHORT)
            .show()
    }

    fun showSnackbar(view: View, @StringRes resId: Int) {
        val text = view.context.getString(resId)
        showSnackbar(view, text)
    }

    fun showSnackbar(view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
        makeSnackbar(view, text, duration)
            .show()
    }

    fun makeSnackbar(view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
        val context = view.context
        val snackBar = Snackbar.make(view, text, duration)
        val snackBarView = snackBar.view

        val bgColor = ContextCompat.getColor(context, R.color.colorTextPrimary)
        ViewCompat.setBackgroundTintList(snackBarView, ColorStateList.valueOf(bgColor))

        snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            ?.setTextColor(ContextCompat.getColor(context, R.color.colorTextLight))

        return snackBar
    }

    fun shareApp(context: Context) {
        val message = context.getString(R.string.app_name) +
                "\n https://play.google.com/store/apps/details?id=${context.packageName}"

        share(context, message)
    }

    fun share(context: Context, message: String) {
        try {
            val title = context.getString(R.string.share)
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.type = "text/plain"
            context.startActivity(Intent.createChooser(sendIntent, title))
        } catch (e: Exception) {
            LogUtils.log("Share", "Fail then sharing", e)
        }
    }

}
