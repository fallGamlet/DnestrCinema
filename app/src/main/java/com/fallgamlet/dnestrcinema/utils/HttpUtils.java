package com.fallgamlet.dnestrcinema.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by fallgamlet on 22.03.17.
 */

public abstract class HttpUtils {
    public static String getAbsoluteUrl(String baseUrl, String url) {
        if (url == null) {
            return null;
        }

        url = url.trim();
        if (url.toLowerCase().startsWith("http://")) {
            return url;
        }

        if (baseUrl == null) {
            return null;
        }

        baseUrl = baseUrl.trim();

        String dopSlash = url.startsWith("/") ? "": "/";
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        }
        return baseUrl+dopSlash+url;
    }

    public static Spanned fromHtml(String html) {
        if (html == null) {
            return null;
        }
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
