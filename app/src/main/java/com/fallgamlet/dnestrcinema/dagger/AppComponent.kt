package com.fallgamlet.dnestrcinema.dagger

import com.fallgamlet.dnestrcinema.app.App
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesFragment
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesFragment
import com.fallgamlet.dnestrcinema.ui.news.NewsFragment
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

    fun inject(fragment: TodayMoviesFragment)

    fun inject(fragment: SoonMoviesFragment)

    fun inject(fragment: NewsFragment)

}
