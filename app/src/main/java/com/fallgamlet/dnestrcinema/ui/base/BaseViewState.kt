package com.fallgamlet.dnestrcinema.ui.base

import androidx.lifecycle.MutableLiveData

abstract class BaseViewState(
    val error: MutableLiveData<Throwable> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData()
)
