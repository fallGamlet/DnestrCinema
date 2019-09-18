package com.fallgamlet.dnestrcinema.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


object LiveDataUtils {

    fun <T> wrapMutable(liveData: LiveData<T>): MediatorLiveData<T> {
        val mediator = MediatorLiveData<T>()

        mediator.addSource(liveData) { mediator.setValue(it) }

        return mediator
    }

    fun <T> getMerged(liveDataList: List<LiveData<T>>): LiveData<T> {
        val liveDataMerger = MediatorLiveData<T>()

        liveDataList.forEach { item ->
            liveDataMerger.addSource<T>(item) { liveDataMerger.setValue(it) }
        }

        return liveDataMerger
    }

    fun <T> sendOneAndNull(liveData: MutableLiveData<T>?, value: T) {
        if (liveData == null)
            return

        liveData.value = value
        liveData.value = null
    }

    fun refreshSignal(vararg liveData: MutableLiveData<*>) {
        liveData.forEach {
            it.value = it.value
        }
    }

}
