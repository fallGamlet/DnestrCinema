package com.fallgamlet.dnestrcinema.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fallgamlet.dnestrcinema.R;


public class ViewUtils {

    public static void hideKeyboard(Activity activity, View v) {
        if (activity == null) {
            return;
        }

        if (v == null) {
            v = activity.getWindow().getCurrentFocus();
        }

        if (v == null) {
            return;
        }

        hideKeyboard((Context) activity, v);
    }

    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity, View v) {
        if (activity == null) {
            return;
        }

        if (v == null) {
            v = activity.getWindow().getCurrentFocus();
        }

        if (v == null) {
            return;
        }

        showKeyboard(activity, v);
    }

    public static void showKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT)
             .show();
    }

    public static void showSnackbar(@NonNull View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show();
    }

    public static void shareApp(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.app_name));
        builder.append(" \n ");
        builder.append("https://play.google.com/store/apps/details?id=");
        builder.append(context.getString(R.string.app_id));

        String message = builder.toString();

        share(context, message);
    }

    public static void share(Context context, String message) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Поделиться"));
        } catch (Exception e) {
            LogUtils.log("Share", "Fail then sharing", e);
        }
    }


    public static void setVisible(@NonNull View view, boolean visible) {
        setVisible(view, visible, View.GONE);
    }

    public static void setVisible(@NonNull View view, boolean visible, int invisibleState) {
        view.setVisibility(visible? View.VISIBLE: invisibleState);
    }

}
