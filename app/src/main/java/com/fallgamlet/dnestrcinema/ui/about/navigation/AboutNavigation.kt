package com.fallgamlet.dnestrcinema.ui.about.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.data.network.KinoTir
import com.fallgamlet.dnestrcinema.ui.ImageActivity
import com.fallgamlet.dnestrcinema.ui.about.AboutScreenState
import com.fallgamlet.dnestrcinema.ui.about.composable.AboutScreen
import com.fallgamlet.dnestrcinema.ui.navigation.RouteDestination
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.IntentUtils

fun NavGraphBuilder.aboutNavigation(
    navController: NavController,
    viewModelFactory: () -> ViewModelProvider.Factory,
    context: () -> Context,
) {
    val state = context().getState()

    composable(route = RouteDestination.About.route) {
        AboutScreen(
            state = state,
            callPhoneAction = { phone ->
                IntentUtils.callPhone(context(), phone)
            }
        )
    }
}

private fun Context.getState(): AboutScreenState {
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
            IntentUtils.sendEmail(this, value)
        },
    )
}

private fun Context.navigateToRoomByPath(path: String) {
    val baseUrl = AppFacade.instance.requestFactory?.baseUrl ?: return
    val imgURL = HttpUtils.getAbsoluteUrl(baseUrl, path)
    val bundle = Bundle().apply {
        putString(ImageActivity.ARG_IMG_URL, imgURL)
    }

    val intent = Intent(this, ImageActivity::class.java).putExtras(bundle)
    startActivity(intent)
}

private fun Context.navigateToMap() {
    IntentUtils.showMap(this, 46.8367399, 29.6142111, "Кинотеатр Тирасполь")
}
