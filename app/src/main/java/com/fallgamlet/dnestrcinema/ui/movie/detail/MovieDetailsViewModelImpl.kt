package com.fallgamlet.dnestrcinema.ui.movie.detail

import androidx.lifecycle.ViewModel
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.ui.mergers.MovieMerger
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.reactive.mapTrue
import com.fallgamlet.dnestrcinema.utils.reactive.schedulersIoToUi
import com.fallgamlet.dnestrcinema.utils.reactive.subscribeDisposable


class MovieDetailsViewModelImpl : ViewModel() {

    val viewState = MovieDetailsViewState()
    private var movie: MovieItem = MovieItem()


    fun setData(item: MovieItem?) {
        movie = item ?: MovieItem()
        showData()
        loadData()
    }

    private fun showData() {
        val details = movie.details
        val baseUrl = AppFacade.instance.requestFactory?.baseUrl

        viewState.title.value = movie.title
        viewState.pubDate.value = movie.pubDate
        viewState.rooms.value = getRoomsSchedule()
        viewState.duration.value = movie.duration
        viewState.director.value = details.director
        viewState.actors.value = details.actors
        viewState.scenario.value = details.scenario
        viewState.ageLimit.value = details.ageLimit
        viewState.budget.value = details.budget
        viewState.country.value = details.country
        viewState.genre.value = details.genre
        viewState.description.value = details.description // HttpUtils.fromHtml(detail.description)

        viewState.trailerMovieUrls.value = movie.trailerUrlSet.toList()
        viewState.posterUrl.value = HttpUtils.getAbsoluteUrl(baseUrl, movie.posterUrl)
        viewState.imageUrls.value = details.imgUrls
            .map { HttpUtils.getAbsoluteUrl(baseUrl, it) ?: "" }
            .toList()
    }

    private fun getRoomsSchedule(): List<String> {
        val rooms = mutableListOf<String>()
        val movieItem = movie
        if (movieItem.schedules.isEmpty()) {
            return rooms
        }

        val strBuilder = StringBuilder()

        for (schedule in movieItem.schedules) {
            strBuilder.append(schedule.room)
            if (schedule.value != null) {
                strBuilder.append(": ").append(schedule.value.trim { it <= ' ' })
            }
            rooms.add(strBuilder.toString())
            strBuilder.setLength(0)
        }

        return rooms
    }

    fun loadData() {
        val urlStr = movie.link ?: return
        val client = AppFacade.instance.netClient ?: return

        client.getDetailMovies(urlStr)
            .schedulersIoToUi()
            .doOnNext {
                MovieMerger().merge(movie, it)
                showData()
            }
            .doOnError { viewState.error.value = it }
            .mapTrue()
            .doOnComplete {
                viewState.loading.value = false
            }
            .subscribeDisposable()
    }

    fun getRoomNames(): List<String> {
        return movie.schedules
            .map { it.room }
            .toList()
    }

}
