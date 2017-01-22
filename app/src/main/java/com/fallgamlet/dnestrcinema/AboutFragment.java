package com.fallgamlet.dnestrcinema;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fallgamlet.dnestrcinema.network.DataSettings;
import com.fallgamlet.dnestrcinema.network.RssItem;


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
    private View mRootView;
    private View mPhoneAutoAnswerView1;
    private View mPhoneAutoAnswerView2;
    private View mPhoneCashboxView;
    private View mEmailDeveloperView;
    private View mRoomBlueView;
    private View mRoomBordoView;
    private View mRoomDvdView;
    private View mPointView;

    private OnFragmentInteractionListener mListener;

    private String mOrgPhoneAutoanswer1;
    private String mOrgPhoneAutoanswer2;
    private String mOrgCashbox;
    private String mEmailDeveloper;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews(inflater, container);

        mOrgPhoneAutoanswer1 = getString(R.string.phone_org_autoanswer_1);
        mOrgPhoneAutoanswer2 = getString(R.string.phone_org_autoanswer_2);
        mOrgCashbox = getString(R.string.phone_org_cashbox);
        mEmailDeveloper = getString(R.string.developer_email);

        fillContactData();

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

    protected void initViews(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false);

        mPhoneAutoAnswerView1 = mRootView.findViewById(R.id.phoneAutoAnswer1);
        mPhoneAutoAnswerView2 =  mRootView.findViewById(R.id.phoneAutoAnswer2);
        mPhoneCashboxView =  mRootView.findViewById(R.id.phoneCashbox);
        mEmailDeveloperView = mRootView.findViewById(R.id.emailDeveloper);

        mRoomBlueView = mRootView.findViewById(R.id.roomBlueView);
        mRoomBordoView = mRootView.findViewById(R.id.roomBordoView);
        mRoomDvdView = mRootView.findViewById(R.id.roomDvdView);

        mPointView = mRootView.findViewById(R.id.pointLayout);

        if (mRoomBlueView != null) { mRoomBlueView.setOnClickListener(this); }
        if (mRoomBordoView != null) { mRoomBordoView.setOnClickListener(this); }
        if (mRoomDvdView != null) { mRoomDvdView.setOnClickListener(this); }
        if (mPointView != null) { mPointView.setOnClickListener(this); }
    }

    protected void fillContactData() {
        //region Set contact data
        if (mPhoneAutoAnswerView1 != null) {
            mPhoneAutoAnswerView1.setOnClickListener(this);
            boolean check = false;
            TextView textView =  (TextView) mPhoneAutoAnswerView1.findViewById(R.id.textView);
            if (textView != null) {
                String value = mOrgPhoneAutoanswer1;
                textView.setText(value);
                check = value != null && !value.isEmpty();
            }
            mPhoneAutoAnswerView1.setVisibility(check? View.VISIBLE: View.GONE);
        }

        if (mPhoneAutoAnswerView2 != null) {
            mPhoneAutoAnswerView2.setOnClickListener(this);
            boolean check = false;
            TextView textView =  (TextView) mPhoneAutoAnswerView2.findViewById(R.id.textView);
            if (textView != null) {
                String value = mOrgPhoneAutoanswer2;
                textView.setText(value);
                check = value != null && !value.isEmpty();
            }
            mPhoneAutoAnswerView2.setVisibility(check? View.VISIBLE: View.GONE);
        }

        if (mPhoneCashboxView != null) {
            mPhoneCashboxView.setOnClickListener(this);
            boolean check = false;
            TextView textView =  (TextView) mPhoneCashboxView.findViewById(R.id.textView);
            if (textView != null) {
                String value = mOrgCashbox;
                textView.setText(value);
                check = value != null && !value.isEmpty();
            }
            mPhoneCashboxView.setVisibility(check? View.VISIBLE: View.GONE);
        }

        if (mEmailDeveloperView != null) {
            mEmailDeveloperView.setOnClickListener(this);
            boolean check = false;
            TextView textView =  (TextView) mEmailDeveloperView.findViewById(R.id.textView);
            if (textView != null) {
                String value = mEmailDeveloper;
                textView.setText(value);
                check = value != null && !value.isEmpty();
            }
            mEmailDeveloperView.setVisibility(check? View.VISIBLE: View.GONE);
        }
        //endregion
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
                new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog)
                        .setTitle(R.string.error)
                        .setMessage(R.string.msg_not_found_intent_for_email)
                        .create()
                        .show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == null) { return; }

        if (view == mPhoneAutoAnswerView1 || view == mPhoneAutoAnswerView2 || view == mPhoneCashboxView) {
            TextView phoneTextView = (TextView) view.findViewById(R.id.textView);
            if (phoneTextView != null) {
                callPhone(phoneTextView.getText().toString());
            }
            return;
        }

        if (view == mEmailDeveloperView) {
            TextView emailTextView = (TextView) view.findViewById(R.id.textView);
            if (emailTextView != null) {
                sendEmail(emailTextView.getText().toString());
            }
            return;
        }

        if (view == mRoomBlueView) {
            navigateToRoomView(RssItem.ROOM_BLUE);
            return;
        }

        if (view == mRoomBordoView) {
            navigateToRoomView(RssItem.ROOM_BORDO);
            return;
        }

        if (view == mRoomDvdView) {
            navigateToRoomView(RssItem.ROOM_DVD);
            return;
        }

        if (view == mPointView) {
            navigateToMap();
            return;
        }

    }

    protected  void navigateToRoomView(String roomName) {
        if (roomName == null) {
            return;
        }

        String imgURL = null;
        if (RssItem.ROOM_BLUE.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BLUE;
        } else if (RssItem.ROOM_BORDO.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_BORDO;
        } else if (RssItem.ROOM_DVD.equalsIgnoreCase(roomName)) {
            imgURL = DataSettings.PATH_IMG_ROOM_DVD;
        }

        if (imgURL != null) {
            imgURL = DataSettings.BASE_URL + imgURL;
            Bundle bundle = new Bundle();
            bundle.putString(ImageActivity.ARG_IMG_URL, imgURL);

            Intent intent = new Intent(getContext(), ImageActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    protected void navigateToMap() {
        try {
            String point = "46.8367399,29.6142111";
            String name = Uri.encode("Кинотеатр Тирасполь");
            Uri gmmIntentUri = Uri.parse("geo:"+point+"?z=19&q="+point+"("+name+")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (Exception e) {
            Log.e("Map", e.toString());
            if (e instanceof ActivityNotFoundException) {
                String imgURL = DataSettings.BASE_URL + DataSettings.PATH_IMG_SCHEME;
                ImageActivity.showActivity(getContext(), imgURL);
            }
        }


    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
