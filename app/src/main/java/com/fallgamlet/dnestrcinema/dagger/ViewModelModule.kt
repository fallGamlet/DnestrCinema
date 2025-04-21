package com.fallgamlet.dnestrcinema.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesViewModel
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesViewModel
import com.fallgamlet.dnestrcinema.ui.news.NewsViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TodayMoviesViewModel::class)
    fun bindTodayMoviesViewModel(viewModel: TodayMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SoonMoviesViewModel::class)
    fun bindSoonMoviesViewModel(viewModel: SoonMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModelImpl::class)
    fun bindNewsViewModel(viewModel: NewsViewModelImpl): ViewModel

}
