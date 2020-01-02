package com.fallgamlet.dnestrcinema.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.data.network.KinoTir
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.mvp.views.Fragments
import com.fallgamlet.dnestrcinema.ui.ImageActivity
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.IntentUtils


open class AboutFragment : Fragments.MvpAboutViewFragment() {

    private lateinit var phoneAutoAnswerView1: View
    private lateinit var phoneAutoAnswerView2: View
    private lateinit var phoneCashboxView: View
    private lateinit var emailDeveloperView: View
    private lateinit var roomBlueView: View
    private lateinit var roomBordoView: View
    private lateinit var roomDvdView: View
    private lateinit var pointView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        fillContactData()
    }

    private fun initViews(view: View) {
        phoneAutoAnswerView1 = view.findViewById(R.id.phoneAutoAnswer1)
        phoneAutoAnswerView2 = view.findViewById(R.id.phoneAutoAnswer2)
        phoneCashboxView = view.findViewById(R.id.phoneCashbox)
        emailDeveloperView = view.findViewById(R.id.emailDeveloper)
        roomBlueView = view.findViewById(R.id.roomBlueView)
        roomBordoView = view.findViewById(R.id.roomBordoView)
        roomDvdView = view.findViewById(R.id.roomDvdView)
        pointView = view.findViewById(R.id.pointLayout)

        roomBlueView.setOnClickListener { navigateToRoomView(MovieItem.ROOM_BLUE) }
        roomBordoView.setOnClickListener { navigateToRoomView(MovieItem.ROOM_BORDO) }
        roomDvdView.setOnClickListener { navigateToRoomView(MovieItem.ROOM_DVD) }
        pointView.setOnClickListener { navigateToMap() }
    }

    private fun fillContactData() {
        listOf(
            Pair(phoneAutoAnswerView1, getString(R.string.phone_org_autoanswer_1)),
            Pair(phoneAutoAnswerView2, getString(R.string.phone_org_autoanswer_2)),
            Pair(phoneCashboxView, getString(R.string.phone_org_cashbox))
        ).forEach {
            val value = it.second
            val textView = it.first.findViewById<TextView>(R.id.textView)
            textView.text = value
            it.first.setOnClickListener { IntentUtils.callPhone(context, value) }
        }

        emailDeveloperView.apply {
            val textView = findViewById<TextView>(R.id.textView)
            val value = getString(R.string.developer_email)
            textView.text = value
            setOnClickListener { IntentUtils.sendEmail(context, value) }
        }
    }

    private fun navigateToRoomView(roomName: String?) {
        roomName ?: return

        val path = when(roomName.toLowerCase()) {
            MovieItem.ROOM_BLUE.toLowerCase() -> KinoTir.PATH_IMG_ROOM_BLUE
            MovieItem.ROOM_BORDO.toLowerCase() -> KinoTir.PATH_IMG_ROOM_BORDO
            MovieItem.ROOM_DVD.toLowerCase() -> KinoTir.PATH_IMG_ROOM_DVD
            else -> return
        }

        val baseUrl = AppFacade.instance.requestFactory?.baseUrl ?: return
        val imgURL = HttpUtils.getAbsoluteUrl(baseUrl, path)
        val bundle = Bundle().apply {
            putString(ImageActivity.ARG_IMG_URL, imgURL)
        }

        val intent = Intent(context, ImageActivity::class.java).putExtras(bundle)
        startActivity(intent)
    }

    private fun navigateToMap() {
        IntentUtils.showMap(context, 46.8367399, 29.6142111, "Кинотеатр Тирасполь")
    }
}
