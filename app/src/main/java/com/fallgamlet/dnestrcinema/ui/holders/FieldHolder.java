package com.fallgamlet.dnestrcinema.ui.holders;

import android.view.View;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

/**
 * Created by fallgamlet on 22.03.17.
 */

public class FieldHolder {
    private static final int FIELD_KEY_ID = R.id.keyView;
    private static final int FIELD_VALUE_ID = R.id.valueView;


    private View rootView;
    private TextView keyView;
    private TextView valueView;


    public FieldHolder() {

    }

    public FieldHolder(View view) {
        initViews(view);
    }

    public FieldHolder(View view, int keyViewID, int valueViewID) {
        initViews(view, keyViewID, valueViewID);
    }


    public View getRootView() {
        return rootView;
    }

    public void setRootView(View view) {
        rootView = view;
    }

    public TextView getKeyView() {
        return keyView;
    }

    public void setKeyView(TextView textView) {
        keyView = textView;
    }

    public TextView getValueView() {
        return valueView;
    }

    public void setValueView(TextView textView) {
        valueView = textView;
    }


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

    public void setData(CharSequence key, CharSequence value) {
        setKey(key);
        setValue(value);
    }

    public void setKey(CharSequence value) {
        if (keyView != null) {
            keyView.setText(value);
        }
    }

    public void setValue(CharSequence value) {
        if (valueView != null) {
            valueView.setText(value);
        }
    }

    public boolean setDataAndVisible(CharSequence key, CharSequence value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            setVisible(false);
            return false;
        }

        setData(key, value);
        setVisible(true);
        return true;
    }

}
