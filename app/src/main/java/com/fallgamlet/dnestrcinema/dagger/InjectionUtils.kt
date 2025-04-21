package com.fallgamlet.dnestrcinema.dagger

import android.content.Context
import androidx.fragment.app.Fragment

fun Context.getAppComponent(): AppComponent {
    val provider = this as? AppComponentProvider
        ?: applicationContext as? AppComponentProvider
        ?: throw RuntimeException(
            "Context or Application must implement of AppComponentProvider contract"
        )

    return provider.provideComponent()
}

fun Fragment.getAppComponent(): AppComponent {
    return requireContext().getAppComponent()
}
