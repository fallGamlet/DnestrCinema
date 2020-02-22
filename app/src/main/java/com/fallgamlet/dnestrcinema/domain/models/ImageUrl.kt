package com.fallgamlet.dnestrcinema.domain.models

data class ImageUrl(
    var hight: String = "",
    var low: String = ""
) {

    fun isEmpty() = this == EMPTY

    companion object {
        val EMPTY = ImageUrl()
    }
}
