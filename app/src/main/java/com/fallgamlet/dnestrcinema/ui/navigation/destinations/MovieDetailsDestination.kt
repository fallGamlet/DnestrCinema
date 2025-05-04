package com.fallgamlet.dnestrcinema.ui.navigation.destinations

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDestination(
    val link: String
) : DestinationMarker
