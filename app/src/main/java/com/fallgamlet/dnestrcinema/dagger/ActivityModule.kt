package com.fallgamlet.dnestrcinema.dagger

import com.fallgamlet.dnestrcinema.ui.start.StartActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {
    @ContributesAndroidInjector
    fun contributeStartActivity(): StartActivity
}
