package com.fallgamlet.dnestrcinema.ui.movie.today

import androidx.lifecycle.MutableLiveData
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.ui.base.BaseViewState

class TodayMoviesViewState(
    val movies: MutableLiveData<List<MovieItem>> = MutableLiveData()
): BaseViewState()
