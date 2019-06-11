package com.fallgamlet.dnestrcinema.ui.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible

import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.mvp.views.Fragments
import com.fallgamlet.dnestrcinema.data.network.KinoTir
import com.fallgamlet.dnestrcinema.ui.ImageActivity
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.LogUtils




class AboutFragment : Fragments.MvpAboutViewFragment(), View.OnClickListener {

    private lateinit var mRootView: View
    private lateinit var mPhoneAutoAnswerView1: View
    private lateinit var mPhoneAutoAnswerView2: View
    private lateinit var mPhoneCashboxView: View
    private lateinit var mEmailDeveloperView: View
    private lateinit var mRoomBlueView: View
    private lateinit var mRoomBordoView: View
    private lateinit var mRoomDvdView: View
    private lateinit var mPointView: View

    private var mListener: OnFragmentInteractionListener? = null

    private var mOrgPhoneAutoanswer1: String? = null
    private var mOrgPhoneAutoanswer2: String? = null
    private var mOrgCashbox: String? = null
    private var mEmailDeveloper: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViews(inflater, container)

        mOrgPhoneAutoanswer1 = getString(R.string.phone_org_autoanswer_1)
        mOrgPhoneAutoanswer2 = getString(R.string.phone_org_autoanswer_2)
        mOrgCashbox = getString(R.string.phone_org_cashbox)
        mEmailDeveloper = getString(R.string.developer_email)

        fillContactData()

        return mRootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            mListener = null
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    protected fun initViews(inflater: LayoutInflater, container: ViewGroup?) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false)

        mPhoneAutoAnswerView1 = mRootView.findViewById(R.id.phoneAutoAnswer1)
        mPhoneAutoAnswerView2 = mRootView.findViewById(R.id.phoneAutoAnswer2)
        mPhoneCashboxView = mRootView.findViewById(R.id.phoneCashbox)
        mEmailDeveloperView = mRootView.findViewById(R.id.emailDeveloper)

        mRoomBlueView = mRootView.findViewById(R.id.roomBlueView)
        mRoomBordoView = mRootView.findViewById(R.id.roomBordoView)
        mRoomDvdView = mRootView.findViewById(R.id.roomDvdView)

        mPointView = mRootView.findViewById(R.id.pointLayout)

        mRoomBlueView.setOnClickListener(this)
        mRoomBordoView.setOnClickListener(this)
        mRoomDvdView.setOnClickListener(this)
        mPointView.setOnClickListener(this)
    }

    protected fun fillContactData() {
        if (mPhoneAutoAnswerView1 != null) {
            mPhoneAutoAnswerView1!!.setOnClickListener(this)
            val textView = mPhoneAutoAnswerView1.findViewById<TextView>(R.id.textView)
            val value = mOrgPhoneAutoanswer1
            textView.text = value
            mPhoneAutoAnswerView1.isVisible = !value.isNullOrBlank()
        }

        if (mPhoneAutoAnswerView2 != null) {
            mPhoneAutoAnswerView2.setOnClickListener(this)
            val textView = mPhoneAutoAnswerView2.findViewById<TextView>(R.id.textView)
            val value = mOrgPhoneAutoanswer2
            textView.text = value
            mPhoneAutoAnswerView2.isVisible = !value.isNullOrBlank()
        }

        if (mPhoneCashboxView != null) {
            mPhoneCashboxView.setOnClickListener(this)
            val textView = mPhoneCashboxView.findViewById<TextView>(R.id.textView)
            val value = mOrgCashbox
            textView.text = value
            mPhoneCashboxView.isVisible = !value.isNullOrBlank()
        }

        if (mEmailDeveloperView != null) {
            mEmailDeveloperView.setOnClickListener(this)
            val textView = mEmailDeveloperView.findViewById<TextView>(R.id.textView)
            val value = mEmailDeveloper
            textView.text = value
            mEmailDeveloperView.isVisible = !value.isNullOrBlank()
        }
    }

    protected fun callPhone(phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        } catch (ignored: Exception) {
            Log.d("Intent onStart", "Type: tel, Value: $phone. Error: $ignored")
        }

    }

    protected fun sendEmail(email: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", email, null))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Приложение К.Тирасполь - Афиша")
            startActivity(intent)
        } catch (ignored: Exception) {
            LogUtils.log("IntentStart", "Type: email, Value: $email fail intent start", ignored)
            AlertDialog.Builder(context!!, R.style.AppTheme_Dialog)
                .setTitle(R.string.error)
                .setCancelable(true)
                .setMessage(R.string.msg_not_found_intent_for_email)
                .create()
                .show()
        }

    }

    override fun onClick(view: View?) {
        if (view == null) {
            return
        }

        if (view === mPhoneAutoAnswerView1 || view === mPhoneAutoAnswerView2 || view === mPhoneCashboxView) {
            val phoneTextView = view.findViewById<View>(R.id.textView) as TextView
            callPhone(phoneTextView.text.toString())
            return
        }

        if (view === mEmailDeveloperView) {
            val emailTextView = view.findViewById<View>(R.id.textView) as TextView
            sendEmail(emailTextView.text.toString())
            return
        }

        if (view === mRoomBlueView) {
            navigateToRoomView(MovieItem.ROOM_BLUE)
            return
        }

        if (view === mRoomBordoView) {
            navigateToRoomView(MovieItem.ROOM_BORDO)
            return
        }

        if (view === mRoomDvdView) {
            navigateToRoomView(MovieItem.ROOM_DVD)
            return
        }

        if (view === mPointView) {
            navigateToMap()
            return
        }

    }

    protected fun navigateToRoomView(roomName: String?) {
        if (roomName == null) {
            return
        }

        var imgURL: String? = null
        if (MovieItem.ROOM_BLUE.equals(roomName, ignoreCase = true)) {
            imgURL = KinoTir.PATH_IMG_ROOM_BLUE
        } else if (MovieItem.ROOM_BORDO.equals(roomName, ignoreCase = true)) {
            imgURL = KinoTir.PATH_IMG_ROOM_BORDO
        } else if (MovieItem.ROOM_DVD.equals(roomName, ignoreCase = true)) {
            imgURL = KinoTir.PATH_IMG_ROOM_DVD
        }

        if (imgURL != null) {
            val baseUrl = AppFacade.instance.requestFactory!!.baseUrl
            imgURL = HttpUtils.getAbsoluteUrl(baseUrl, imgURL)
            val bundle = Bundle()
            bundle.putString(ImageActivity.ARG_IMG_URL, imgURL)

            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

    }

    protected fun navigateToMap() {
        try {
            val point = "46.8367399,29.6142111"
            val name = Uri.encode("Кинотеатр Тирасполь")
            val gmmIntentUri = Uri.parse("geo:$point?z=19&q=$point($name)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } catch (e: Exception) {
            Log.e("Map", e.toString())
        }

    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}
