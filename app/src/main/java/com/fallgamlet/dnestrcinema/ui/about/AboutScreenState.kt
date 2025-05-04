package com.fallgamlet.dnestrcinema.ui.about

data class AboutScreenState(
    val rooms: List<Room>,
    val contactInfo: String,
    val supportLabel: String,
    val supportPhones: List<String>,
    val cashBoxLabel: String,
    val cashBoxPhones: List<String>,
    val address: String,
    val addressAction: () -> Unit,
    val attentionInfo: String,
    val shareAppLabel: String,
    val shareAppAction: () -> Unit,
    val devInfo: String,
    val devEmail: String,
    val devEmailAction: () -> Unit,
) {
    data class Room(
        val title: String,
        val action: () -> Unit,
    )
}
