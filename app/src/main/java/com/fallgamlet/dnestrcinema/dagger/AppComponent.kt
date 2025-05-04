package com.fallgamlet.dnestrcinema.dagger

import com.fallgamlet.dnestrcinema.app.App
import com.fallgamlet.dnestrcinema.ui.start.StartActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setApp(app: App): Builder
        fun build(): AppComponent
    }

    fun inject(activity: StartActivity)

}
