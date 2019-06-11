package com.fallgamlet.dnestrcinema.utils

import java.util.ArrayList
import java.util.HashSet


object CollectionUtils {

    fun isEmpty(collection: Collection<*>?): Boolean {
        return collection == null || collection.isEmpty()
    }

    fun getSize(collection: Collection<*>?): Int {
        return collection?.size ?: 0
    }

    fun <T> getNonNull(list: List<T>?): List<T> {
        return list ?: ArrayList()
    }

    fun <T> getNonNull(list: Set<T>?): Set<T> {
        return list ?: HashSet()
    }

    fun <T> getList(set: Set<T>?): List<T> {
        return set?.let { ArrayList(it) } ?: ArrayList()
    }

    fun <T> emptyList(): List<T> {
        return ArrayList()
    }

    fun <T, R> mapItems(items: Collection<T>, converter: Converter<T, R>): List<R> {
        if (isEmpty(items)) {
            return emptyList()
        }

        val resultItems = ArrayList<R>(items.size)

        for (item in items) {
            val resItem = converter.convert(item)
            if (item != null) {
                resultItems.add(resItem)
            }
        }

        return resultItems
    }

    fun <T> forEach(items: Collection<T>, function: Each<T>) {
        if (isEmpty(items)) {
            return
        }

        for (item in items) {
            function.run(item)
        }
    }


    interface Converter<T, R> {
        fun convert(item: T?): R
    }

    interface Each<T> {
        fun run(item: T)
    }
}
