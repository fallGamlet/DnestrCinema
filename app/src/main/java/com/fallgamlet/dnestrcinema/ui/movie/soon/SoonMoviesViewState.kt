package com.fallgamlet.dnestrcinema.ui.movie.soon

import androidx.lifecycle.MutableLiveData
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.ui.base.BaseViewState

class SoonMoviesViewState(
    val movies: MutableLiveData<List<MovieItem>> = MutableLiveData()
): BaseViewState()
