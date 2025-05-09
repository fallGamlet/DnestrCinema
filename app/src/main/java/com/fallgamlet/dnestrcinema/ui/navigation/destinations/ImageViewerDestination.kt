package com.fallgamlet.dnestrcinema.ui.navigation.destinations

import kotlinx.serialization.Serializable

@Serializable
data class ImageViewerDestination(
    val link: String
) : DestinationMarker
