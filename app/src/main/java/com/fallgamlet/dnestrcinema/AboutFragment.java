package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    //region Fields
    View mRootView;
    TextView mPhoneAutoAnswerView1;
    TextView mPhoneAutoAnswerView2;
    TextView mPhoneCashboxView;
    TextView mEmailDeveloperView;
    private OnFragmentInteractionListener mListener;
    //endregion

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false);

        mPhoneAutoAnswerView1 = (TextView) mRootView.findViewById(R.id.phoneAutoAnswer1);
        mPhoneAutoAnswerView2 = (TextView) mRootView.findViewById(R.id.phoneAutoAnswer2);
        mPhoneCashboxView = (TextView) mRootView.findViewById(R.id.phoneCashbox);
        mEmailDeveloperView = (TextView) mRootView.findViewById(R.id.emailDeveloper);

        mPhoneAutoAnswerView1.setOnClickListener(this);
        mPhoneAutoAnswerView2.setOnClickListener(this);
        mPhoneCashboxView.setOnClickListener(this);
        mEmailDeveloperView.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            mListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void callPhone(String phone) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        } catch (Exception ignored) {
            Log.d("Intent start", "Type: tel, Value: "+phone+". Error: "+ignored.toString());
        }
    }

    protected void sendEmail(String email) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", email, null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Приложение К.Тирасполь - Афиша");
            startActivity(intent);
        } catch (Exception ignored) {
            Log.d("Intent start", "Type: email, Value: "+email+". Error: "+ignored.toString());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == null) { return; }

        if (view == mPhoneAutoAnswerView1 || view == mPhoneAutoAnswerView2 || view == mPhoneCashboxView) {
            callPhone(((TextView)view).getText().toString());
            return;
        }

        if (view == mEmailDeveloperView) {
            CharSequence val = ((TextView)view).getText();
            sendEmail(val.toString());
            return;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
