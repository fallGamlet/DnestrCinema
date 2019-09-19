package com.fallgamlet.dnestrcinema.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import com.fallgamlet.dnestrcinema.R


object IntentUtils {

    fun startPhoneCall(context: Context?, phone: String?) {
        if (context == null) return
        if (phone.isNullOrEmpty()) return
        try {
            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.err_msg_phone_caller_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    fun startEmail(context: Context?, email: String?) {
        if (context == null) return
        if (email.isNullOrEmpty()) return
        try {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email_chooser_title)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.err_msg_email_sender_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    fun openSite(context: Context?, url: String?) {
        if (context == null) return
        if (url.isNullOrEmpty()) return
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.err_msg_webbrowser_not_found), Toast.LENGTH_SHORT).show()
        }
    }

//    fun showGoogleMap(context: Context?, startPoint: LatLng, endPoint: LatLng?) {
//        if (context == null) return
//        try {
//            val uriBuilder = Uri.Builder()
//                    .scheme("http")
//                    .authority("maps.google.com")
//                    .path("maps")
//                    .appendQueryParameter("saddr", "${startPoint.latitude},${startPoint.longitude}")
//
//            if (endPoint != null) {
//                uriBuilder.appendQueryParameter("daddr", "${endPoint.latitude},${endPoint.longitude}")
//            }
//
//            val intent = Intent(Intent.ACTION_VIEW, uriBuilder.build())
//                    .setPackage(GOOGLE_ROUTE_PACKAGE)
//
//            context.startActivity(intent)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(context, context.getString(R.string.err_msg_google_maps_is_no_installed), Toast.LENGTH_SHORT).show()
//        }
//
//    }

    fun isAppInstalled(context: Context?, packageName: String): Boolean {
        return try {
            context?.packageManager?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    private const val GOOGLE_ROUTE_PACKAGE = "com.google.android.apps.maps"

}
