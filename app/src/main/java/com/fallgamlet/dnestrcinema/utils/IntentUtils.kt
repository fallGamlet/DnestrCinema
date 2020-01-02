package com.fallgamlet.dnestrcinema.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment

object IntentUtils {

    fun callPhone(context: Context?, phone: String) {
        try {
            context ?: return
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.log("Intent onStart", "Type: tel, Value: $phone error", e)
        }
    }

    fun sendEmail(context: Context?, email: String) {
        try {
            context ?: return
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.log("Intent onStart", "Type: tel, Value: $email error", e)
        }
    }

    @JvmOverloads
    fun showMap(context: Context?, latitude: Double, longitude: Double, name: String? = "") {
        try {
            context ?: return
            val point = "$latitude,$longitude"
            var url = "geo:$point?z=17"

            if (!name.isNullOrBlank()) {
                url += "&q=" + point + "(" + Uri.encode(name) + ")"
            }

            val intentUri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, intentUri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.setPackage("com.google.android.apps.maps")
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.log("Map", e.toString())
        }

    }

    fun openUrl(context: Context?, url: String) {
        try {
            context ?: return
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.log("Intent onStart", "Open url: $url error", e)
        }

    }


    fun startActivityForResult(activity: Activity, intent: Intent, requestCode: Int, failure: ((Throwable) -> Unit)? = null) {
        try {
            activity.startActivityForResult(intent, requestCode)
        } catch (err: Throwable) {
            failure?.invoke(err)
        }
    }

    fun startActivityForResult(fragment: Fragment, intent: Intent, requestCode: Int, failure: ((Throwable) -> Unit)? = null) {
        try {
            fragment.startActivityForResult(intent, requestCode)
        } catch (err: Throwable) {
            failure?.invoke(err)
        }
    }

    fun startActivity(context: Context, intent: Intent, failure: ((Throwable) -> Unit)? = null) {
        try {
            context.startActivity(intent)
        } catch (err: Throwable) {
            failure?.invoke(err)
        }
    }

    fun chooserIntent(intent: Intent, title: String): Intent {
        return Intent.createChooser(intent, title)
    }

    fun cameraIntent(): Intent {
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }

    fun galleryIntent(): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.type = "image/*"
        return intent
    }

}
