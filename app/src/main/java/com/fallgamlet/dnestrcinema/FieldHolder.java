package com.fallgamlet.dnestrcinema;

import android.view.View;
import android.widget.TextView;

/**
 * Created by fallgamlet on 22.03.17.
 */

public class FieldHolder {
    public static final int FIELD_KEY_ID = R.id.keyView;
    public static final int FIELD_VALUE_ID = R.id.valueView;

    //region Fields
    View rootView;
    TextView keyView;
    TextView valueView;
    //endregion

    //region Getters and setters
    public View getRootView() { return rootView; }
    public void setRootView(View view) { rootView = view; }

    public TextView getKeyView() { return keyView; }
    public void setKeyView(TextView textView) { keyView = textView; }

    public TextView getValueView() { return valueView; }
    public void setValueView(TextView textView) { valueView = textView; }
    //endregion

    //region Methods
    public void initViews(View view) {
        initViews(view, FIELD_KEY_ID, FIELD_VALUE_ID);
    }

    public void initViews(View view, int keyViewID, int valueViewID) {
        setRootView(view);
        if (rootView != null) {
            setKeyView((TextView) view.findViewById(keyViewID));
            setValueView((TextView) view.findViewById(valueViewID));
        }
    }

    public void setVisible(boolean visible) {
        if (rootView != null) {
            rootView.setVisibility(visible? View.VISIBLE: View.GONE);
        }
    }

    public void setData(String key, String value) {
        if (keyView != null) {
            keyView.setText(key);
        }
        if (valueView != null) {
            valueView.setText(value);
        }
    }

    public boolean setDataAndVisible(String key, String value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            setVisible(false);
            return false;
        }
        setData(key, value);
        setVisible(true);
        return true;
    }
    //endregion
}