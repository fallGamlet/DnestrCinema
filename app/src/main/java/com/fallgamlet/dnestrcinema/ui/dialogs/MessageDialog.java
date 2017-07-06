package com.fallgamlet.dnestrcinema.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.R;

public class MessageDialog extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener {



    //region Sub Classes and Interfaces
    public interface DialogListener {
        void onPositiveButton_clicked();
        void onNegativeButton_clicked();
    }
    //endregion

    //region Static Fields
    public static final String PARAM_STATE = "state";

    public static final int STATE_EMPTY = 0;
    public static final int STATE_MESSAGE = 1;
    public static final int STATE_QUESTION = 2;
    public static final int STATE_WAITING = 3;
    public static final int STATE_INPUT = 4;

    public static final String STATE_EMPTY_NAME = "empty";
    public static final String STATE_MESSAGE_NAME = "message";
    public static final String STATE_QUESTION_NAME = "question";
    public static final String STATE_WAITING_NAME = "waiting";
    public static final String STATE_INPUT_NAME = "input";

    protected static final Integer POSITIVE = DialogInterface.BUTTON_POSITIVE;
    protected static final Integer NEGATIVE = DialogInterface.BUTTON_NEGATIVE;
    protected static final Integer NEUTRAL = DialogInterface.BUTTON_NEUTRAL;
    //endregion

    //region Fields
    ViewGroup mRootView;
    DialogListener mDialogListener;
    int mState = STATE_EMPTY;
    FragmentManager mManager;
    LayoutInflater mInflater;
    String mTitle;
    String mText;
    String mPositiveButton;
    String mNegativeButton;
    int mInputType = InputType.TYPE_CLASS_TEXT;

    AppCompatEditText mInputField;
    ProgressBar mProgressBar;
    //endregion

    //region Constructors
    public MessageDialog() {

    }
    //endregion

    //region Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().dismiss();
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity()); //, R.style.AppTheme_Dialog
        dialogBuilder.setTitle(mTitle);
        dialogBuilder.setMessage(mText);
        dialogBuilder.setPositiveButton(R.string.ok, this);
        dialogBuilder.setNegativeButton(R.string.cancel, this);

        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setExtraView(mState);
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            if (mTitle == null) { dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE); }
        }

        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        String btnName = null;
        if (mDialogListener != null) {
            if (POSITIVE == i) {
                mDialogListener.onPositiveButton_clicked();
                btnName = "positive";
            } else if (NEGATIVE == i) {
                mDialogListener.onNegativeButton_clicked();
                btnName = "negative";
            } else if (NEUTRAL == i) {
                btnName = "neutral";
            }
        } else {
            dismiss();
        }
        System.out.println("Dialog button pressed: "+btnName);
    }

    public static MessageDialog newInstance(@NonNull FragmentManager manager, DialogListener listener) {
        MessageDialog dialog = new MessageDialog();
        dialog.setDialogListener(listener);
        dialog.setFragmentMenager(manager);
        return dialog;
    }

    protected void setButtons(int state) {
        if (getContext() == null) {
            return;
        }
        switch (state) {
            case STATE_INPUT:
                if (mPositiveButton == null) { mPositiveButton = getString(R.string.accept); }
                if (mNegativeButton == null) { mNegativeButton = getString(R.string.cancel); }
                break;
            case STATE_QUESTION:
                if (mPositiveButton == null) { mPositiveButton = getString(R.string.yes); }
                if (mNegativeButton == null) { mNegativeButton = getString(R.string.no); }
                break;
            case STATE_MESSAGE:
                if (mPositiveButton == null) { mPositiveButton = getString(R.string.ok); }
                break;
            default:
                break;
        }
    }

    protected void setExtraView(int state) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        setButtons(state);

        View view = null;
        mProgressBar = null;
        mInputField = null;
        boolean cancelable = true;

        ColorDrawable bgColor = new ColorDrawable(Color.WHITE);
        switch (state) {
            case STATE_INPUT:
                view = View.inflate(context, R.layout.dialog_input, null);
                mInputField = (AppCompatEditText) view.findViewById(R.id.inputView);
                break;
            case STATE_WAITING:
                view = View.inflate(context, R.layout.dialog_waiting, null);
                cancelable = false;
                mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                bgColor = new ColorDrawable(Color.TRANSPARENT);
                break;
            case STATE_QUESTION:
                view = View.inflate(context, R.layout.dialog_question, null);
                break;
            case STATE_MESSAGE:
                view = View.inflate(context, R.layout.dialog_message, null);
                break;
            default:
                view = View.inflate(context, R.layout.layout_empty, null);
                break;
        }

        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        TextView msgView = (TextView) view.findViewById(R.id.textView);
        Button posBtn = (Button) view.findViewById(R.id.positiveBtn);
        Button negBtn = (Button) view.findViewById(R.id.negativeBtn);

        if (titleView != null) {
            titleView.setText(mTitle);
            titleView.setVisibility(mTitle==null? View.GONE: View.VISIBLE);
        }
        if (msgView != null) {
            msgView.setText(mText);
            msgView.setVisibility(mText==null? View.GONE: View.VISIBLE);
        }

        if (posBtn != null) {
            posBtn.setText(mPositiveButton);
            posBtn.setVisibility(mPositiveButton==null? View.GONE: View.VISIBLE);
            posBtn.setTag(POSITIVE);
            posBtn.setOnClickListener(this);
        }

        if (negBtn != null) {
            negBtn.setText(mNegativeButton);
            negBtn.setVisibility(mNegativeButton==null? View.GONE: View.VISIBLE);
            negBtn.setTag(NEGATIVE);
            negBtn.setOnClickListener(this);
        }

        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            setCancelable(cancelable);
            dialog.setContentView(view);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(bgColor);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }
    }

    public int getState() {return mState;}

    public String getInputValue() {
        return mInputField == null ? null : mInputField.getText().toString().trim();
    }

    public void setDialogListener(DialogListener listener) {
        mDialogListener = listener;
    }

    public void setFragmentMenager(FragmentManager manager) {
        mManager = manager;
    }

    public static void CreateAndShow(Context context) {
        Intent intent = new Intent(context, MessageDialog.class);
        context.startActivity(intent);
    }

    public boolean showAsDialogIfNeeded() {
        boolean result = false;
        boolean isVisible = isVisible();
        boolean isAdded = isAdded();
        if (!isVisible && !isAdded) {
            if (mManager != null) {
                try {
                    show(mManager, "dialog_"+generateID());
                    result = true;
                } catch (Exception ignored) {
                    result = false;
                }
            }
        }
        return result;
    }

    public void showEmpty() {
        mState = STATE_EMPTY;
        mTitle = null;
        mText = null;
        setButtons(mState);
        setExtraView(mState);
        showAsDialogIfNeeded();
    }

    public void showWaiting(String text) {
        mState = STATE_WAITING;
        mTitle = null;
        mText = text;
        setButtons(mState);
        setExtraView(mState);
        showAsDialogIfNeeded();
    }

    public void showMessage(String text) { showMessage(null, text, null); }
    public void showMessage(String title, String text) { showMessage(title, text, null); }
    public void showMessage(String title, String text, String btnTitle) {
        mState = STATE_MESSAGE;
        mTitle = title;
        mText = text;
        mPositiveButton = btnTitle;
        mNegativeButton = null;
        setExtraView(mState);
        showAsDialogIfNeeded();
    }

    public void showQuestion(String text) { showQuestion(null, text); }
    public void showQuestion(String title, String text) {
        showQuestion(title, text, null, null);
    }
    public void showQuestion(String title, String text, String positiveButton, String negativeButton) {
        mState = STATE_QUESTION;
        mTitle = title;
        mText = text;
        mPositiveButton = positiveButton;
        mNegativeButton = negativeButton;
        setExtraView(mState);
        showAsDialogIfNeeded();
    }

    public void showInputPassword(String text) { showInputPassword(null, text); }
    public void showInputPassword(String title, String text) {
        showInputPassword(title, text, null, null);
    }
    public void showInputPassword(String title, String text, String positiveButton, String negativeButton) {
        mInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        showInputView(title, text, positiveButton, negativeButton);
    }

    public void showInput(String text) { showInput(null, text); }
    public void showInput(String title, String text) {
        showInput(title, text, null, null);
    }
    public void showInput(String title, String text, String positiveButton, String negativeButton) {
        mInputType = InputType.TYPE_CLASS_TEXT;
        showInputView(title, text, positiveButton, negativeButton);
    }
    public void showInputView(String title, String text, String positiveButton, String negativeButton) {
        mState = STATE_INPUT;
        mTitle = title;
        mText = text;
        mPositiveButton = positiveButton;
        mNegativeButton = negativeButton;
        setExtraView(mState);
        showAsDialogIfNeeded();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == POSITIVE) {
            if (mDialogListener != null) {
                mDialogListener.onPositiveButton_clicked();
            } else {
                dismiss();
            }
        } else if (v.getTag() == NEGATIVE) {
            if (mDialogListener != null) {
                mDialogListener.onNegativeButton_clicked();
            } else {
                dismiss();
            }
        }

    }

    public static MessageDialog createAndShowMessage(String text, @NonNull FragmentManager manager, DialogListener listener) {
        MessageDialog msg = MessageDialog.newInstance(manager, listener);
        msg.setCancelable(true);
        msg.showMessage(text);
        return msg;
    }

    //region Generate ID singleton
    private static long __lastid=0;
    protected static long generateID() {
        return ++__lastid;
    }
    //endregion
    //endregion


}
