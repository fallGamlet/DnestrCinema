package com.fallgamlet.dnestrcinema.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.data.network.KinoTir
import com.fallgamlet.dnestrcinema.ui.ImageActivity
import com.fallgamlet.dnestrcinema.ui.about.composable.AboutScreen
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.IntentUtils


class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val state = getState()
        return ComposeView(requireContext()).apply {
            setContent {
                AboutScreen(
                    state = state,
                    callPhoneAction = { phone ->
                        IntentUtils.callPhone(context, phone)
                    }
                )
            }
        }
    }

    private fun getState(): AboutScreenState {
        return AboutScreenState(
            rooms = listOf(
                AboutScreenState.Room(
                    title = getString(R.string.room_blue),
                    action = { navigateToRoomByPath(KinoTir.PATH_IMG_ROOM_BLUE) },
                ),
                AboutScreenState.Room(
                    title = getString(R.string.room_bordo),
                    action = { navigateToRoomByPath(KinoTir.PATH_IMG_ROOM_BORDO) },
                ),
                AboutScreenState.Room(
                    title = getString(R.string.room_dvd),
                    action = { navigateToRoomByPath(KinoTir.PATH_IMG_ROOM_DVD) },
                ),
            ),
            contactInfo = getString(R.string.org_contact_info),
            supportLabel = getString(R.string.label_autoanswer),
            supportPhones = listOf(
                getString(R.string.phone_org_autoanswer_1),
                getString(R.string.phone_org_autoanswer_2),
            ),
            cashBoxLabel = getString(R.string.label_cashbox_with_worktime),
            cashBoxPhones = listOf(
                getString(R.string.phone_org_cashbox),
            ),
            address = getString(R.string.org_address),
            addressAction = { navigateToMap() },
            attentionInfo = getString(R.string.org_desc_info),
            devInfo = getString(R.string.developer_contact_info),
            devEmail = getString(R.string.developer_email),
            devEmailAction = {
                val value = getString(R.string.developer_email)
                IntentUtils.sendEmail(context, value)
            },
        )
    }

    private fun navigateToRoomByPath(path: String) {
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
