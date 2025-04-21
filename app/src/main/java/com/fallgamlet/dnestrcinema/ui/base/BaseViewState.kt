package com.fallgamlet.dnestrcinema.ui.base

import androidx.lifecycle.MutableLiveData

@Deprecated("must use Kotlin Flow for ui state")
abstract class BaseViewState(
    val error: MutableLiveData<Throwable?> = MutableLiveData(),
    val loading: MutableLiveData<Boolean?> = MutableLiveData()
)
