package com.fallgamlet.dnestrcinema.utils


object StringUtils {

    fun isEmpty(value: String?): Boolean {
        return value == null || value.isEmpty()
    }

    fun isEmpty(value: CharSequence?): Boolean {
        return value == null || value.isEmpty()
    }

}
