package com.fallgamlet.dnestrcinema.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailsViewModel
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesViewModel
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesViewModel
import com.fallgamlet.dnestrcinema.ui.news.NewsViewModel
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
    @ViewModelKey(NewsViewModel::class)
    fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel

}
