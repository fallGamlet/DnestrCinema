package com.fallgamlet.dnestrcinema.utils

import android.text.Html
import android.text.Spanned


object HttpUtils {
    fun getAbsoluteUrl(baseUrl: String?, url: String?): String? {
        url ?: return null

        var prefixUrl = baseUrl
        var postfixUrl: String? = url

        postfixUrl = postfixUrl!!.trim { it <= ' ' }
        if (postfixUrl.toLowerCase().startsWith("http://")) {
            return postfixUrl
        }

        prefixUrl ?: return null
        prefixUrl = prefixUrl.trim(' ')

        val dopSlash = if (postfixUrl.startsWith("/")) "" else "/"
        if (prefixUrl.endsWith("/")) {
            prefixUrl = prefixUrl.substring(0, prefixUrl.length - 1)
        }

        return prefixUrl + dopSlash + postfixUrl
    }

    fun fromHtml(html: String?): Spanned? {
        html?: return null

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }
}
