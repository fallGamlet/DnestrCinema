package com.fallgamlet.dnestrcinema.ui.movie.detail

import androidx.lifecycle.MutableLiveData
import com.fallgamlet.dnestrcinema.ui.base.BaseViewState
import java.util.*

class MovieDetailsViewState(
    val title: MutableLiveData<CharSequence?> = MutableLiveData(),
    val pubDate: MutableLiveData<Date?> = MutableLiveData(),
    val rooms: MutableLiveData<List<CharSequence>?> = MutableLiveData(),
    val duration: MutableLiveData<CharSequence?> = MutableLiveData(),
    val director: MutableLiveData<CharSequence?> = MutableLiveData(),
    val actors: MutableLiveData<CharSequence?> = MutableLiveData(),
    val scenario: MutableLiveData<CharSequence?> = MutableLiveData(),
    val ageLimit: MutableLiveData<CharSequence?> = MutableLiveData(),
    val budget: MutableLiveData<CharSequence?> = MutableLiveData(),
    val country: MutableLiveData<CharSequence?> = MutableLiveData(),
    val genre: MutableLiveData<CharSequence?> = MutableLiveData(),
    val description: MutableLiveData<String?> = MutableLiveData(),
    val posterUrl: MutableLiveData<String?> = MutableLiveData(),
    val imageUrls: MutableLiveData<List<String>?> = MutableLiveData(),
    val trailerMovieUrls: MutableLiveData<List<String>?> = MutableLiveData()
) : BaseViewState()
