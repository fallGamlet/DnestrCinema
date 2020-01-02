package com.fallgamlet.dnestrcinema.utils


object StringUtils {

    fun isNotEquals(val1: String, val2: String): Boolean {
        return !isEqual(val1, val2)
    }

    fun isEqual(val1: String?, val2: String?): Boolean {
        return val1 == null && val2 == null || val1 != null && val1 == val2
    }

    fun isEmpty(value: String?): Boolean {
        return value == null || value.isEmpty()
    }

    fun isEmpty(value: CharSequence?): Boolean {
        return value == null || value.isEmpty()
    }

    fun trim(value: String?): String {
        return value?.trim { it <= ' ' } ?: ""
    }

    fun sliceWithPostfix(value: String, end: Int, postfix: String): String? {
        return if (isEmpty(value) || value.length < end)
            value
        else
            slice(value, end) + postfix
    }

    fun slice(value: String, end: Int): String {
        return slice(value, 0, end)
    }

    fun slice(value: String, start: Int, end: Int): String {
        return if (value.length > end) {
            value.substring(start, end)
        } else {
            value
        }
    }

}
