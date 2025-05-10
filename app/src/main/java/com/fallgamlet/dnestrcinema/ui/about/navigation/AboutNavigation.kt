package com.fallgamlet.dnestrcinema.ui.about.navigation

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.data.network.KinoTir
import com.fallgamlet.dnestrcinema.ui.about.AboutScreenState
import com.fallgamlet.dnestrcinema.ui.about.composable.AboutScreen
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.AboutDestination
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.ImageViewerDestination
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.IntentUtils
import com.fallgamlet.dnestrcinema.utils.ViewUtils

fun NavGraphBuilder.aboutNavigation(
    navController: NavController,
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable<AboutDestination> {
        val context = LocalContext.current
        val state = remember { context.getState(navController) }
        AboutScreen(
            state = state,
            callPhoneAction = { phone ->
                IntentUtils.callPhone(context, phone)
            }
        )
    }
}

private fun Context.getState(
    navController: NavController,
): AboutScreenState {
    return AboutScreenState(
        rooms = listOf(
            AboutScreenState.Room(
                title = getString(R.string.room_blue),
                action = { navController.navigateToRoomByPath(KinoTir.PATH_IMG_ROOM_BLUE) },
            ),
            AboutScreenState.Room(
                title = getString(R.string.room_bordo),
                action = { navController.navigateToRoomByPath(KinoTir.PATH_IMG_ROOM_BORDO) },
            ),
            AboutScreenState.Room(
                title = getString(R.string.room_dvd),
                action = { navController.navigateToRoomByPath(KinoTir.PATH_IMG_ROOM_DVD) },
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
        shareAppLabel = getString(R.string.share),
        shareAppAction = { ViewUtils.shareApp(this) },
        devInfo = getString(R.string.developer_contact_info),
        devEmail = getString(R.string.developer_email),
        devEmailAction = {
            val value = getString(R.string.developer_email)
            IntentUtils.sendEmail(this, value)
        },
    )
}

private fun NavController.navigateToRoomByPath(path: String) {
    val baseUrl = "http://kinotir.md"
    val imgURL = HttpUtils.getAbsoluteUrl(baseUrl, path) ?: return

    navigate(ImageViewerDestination(imgURL))
}

private fun Context.navigateToMap() {
    IntentUtils.showMap(this, 46.8367399, 29.6142111, "Кинотеатр Тирасполь")
}
