package com.fallgamlet.dnestrcinema.ui.about.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.ui.about.AboutScreenState

@Composable
fun AboutScreen(
    state: AboutScreenState,
    callPhoneAction: (String) -> Unit
) {
    val spaceMiddle = 16.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(spaceMiddle),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spaceMiddle),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.rooms.forEach { room ->
                Button(
                    onClick = room.action,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .weight(1f),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = room.title,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        ),
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }

        Text(
            text = state.contactInfo,
            modifier = Modifier.padding(top = spaceMiddle),
        )
        Text(
            text = state.supportLabel,
            modifier = Modifier.padding(top = spaceMiddle),
        )

        state.supportPhones.forEach { phone ->
            CallPhone(
                phone = phone,
                onClick = { callPhoneAction(phone) }
            )
        }


        Text(
            text = state.cashBoxLabel,
            modifier = Modifier.padding(top = spaceMiddle),
        )

        state.cashBoxPhones.forEach { phone ->
            CallPhone(
                phone = phone,
                onClick = { callPhoneAction(phone) }
            )
        }

        IconTextLinkButton(
            icon = Icons.Outlined.Place,
            text = state.address,
            onClick = state.addressAction,
        )
        Text(
            text = state.attentionInfo,
            modifier = Modifier.padding(top = spaceMiddle),
        )
        IconTextLinkButton(
            icon = Icons.Outlined.Share,
            text = state.shareAppLabel,
            onClick = state.shareAppAction,
        )
        Text(
            text = state.devInfo,
            modifier = Modifier.padding(top = spaceMiddle),
        )
        SendEmail(
            email = state.devEmail,
            onClick = state.devEmailAction
        )
    }
}

@Composable
private fun SendEmail(
    email: String,
    onClick: () -> Unit,
) {
    IconTextLinkButton(
        icon = Icons.Outlined.Email,
        text = email,
        onClick = onClick,
    )
}

@Composable
private fun CallPhone(
    phone: String,
    onClick: () -> Unit,
) {
    IconTextLinkButton(
        icon = Icons.Outlined.Call,
        text = phone,
        onClick = onClick,
    )
}

@Composable
private fun IconTextLinkButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
        )
    }
}


@Preview
@Composable
private fun PreviewAboutScreen() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        AboutScreen(
            state = AboutScreenState(
                rooms = listOf(
                    AboutScreenState.Room(
                        title = stringResource(R.string.room_blue),
                        action = { }
                    ),
                    AboutScreenState.Room(
                        title = stringResource(R.string.room_bordo),
                        action = { }
                    ),
                    AboutScreenState.Room(
                        title = stringResource(R.string.room_dvd),
                        action = { }
                    ),
                ),
                contactInfo = stringResource(R.string.org_contact_info),
                supportLabel = stringResource(R.string.label_autoanswer),
                supportPhones = listOf(
                    stringResource(R.string.phone_org_autoanswer_1),
                    stringResource(R.string.phone_org_autoanswer_2),
                ),
                cashBoxLabel = stringResource(R.string.label_cashbox_with_worktime),
                cashBoxPhones = listOf(
                    stringResource(R.string.phone_org_cashbox),
                ),
                address = stringResource(R.string.org_address),
                addressAction = { },
                attentionInfo = stringResource(R.string.org_desc_info),
                shareAppLabel = "Share the application",
                shareAppAction = { },
                devInfo = stringResource(R.string.developer_contact_info),
                devEmail = stringResource(R.string.developer_email),
                devEmailAction = { },
            ),
            callPhoneAction = { }
        )
    }
}
