package com.fallgamlet.dnestrcinema.ui.movie.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fallgamlet.dnestrcinema.ui.movie.model.MovieVo
import com.fallgamlet.dnestrcinema.ui.movie.model.ScheduleItemVo
import com.fallgamlet.dnestrcinema.ui.shared.SwipeToRefreshItems
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MoviesComposable(
    movies: List<MovieVo>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (MovieVo) -> Unit,
) {
    SwipeToRefreshItems(
        count = movies.size,
        isRefreshing = isRefreshing,
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh,
        itemContent = { index ->
            val movie = movies[index]
            Column(
                modifier = Modifier.fillMaxWidth().padding(0.dp)
            ) {
                MovieSnippet(
                    movie = movie,
                    onClick = { onClick(movie) },
                )
                if (index != movies.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                }
            }
        },
    )
}

@OptIn(DelicateCoroutinesApi::class)
@Preview
@Composable
private fun MoviesPreview() {
    val movies = (0..9).map {
        MovieVo(
            title = "Movie Title $it",
            link = "",
            duration = "110 minutes",
            pubDate = "from 10 April",
            posterUtl = "https://www.creativeuncut.com/gallery-05/art/at-misc02_s.jpg",
            schedules = listOf(
                ScheduleItemVo(
                    room = "Blue room",
                    time = "14:00, 17:30",
                ),
                ScheduleItemVo(
                    room = "Red room",
                    time = "12:00",
                ),
            ),
        )
    }

    val moviesState = remember { mutableStateOf(movies) }
    val isRefreshing = remember { mutableStateOf(false) }
    val refreshingCount = remember { mutableStateOf(0) }

    MoviesComposable(
        movies = moviesState.value,
        isRefreshing = isRefreshing.value,
        onRefresh = {
            GlobalScope.launch {
                isRefreshing.value = true
                refreshingCount.value++
                delay(1000)
                isRefreshing.value = false
                moviesState.value = moviesState.value.reversed()
            }
        },
        onClick = { movie ->
            Log.d("MyLogs", "onClick $movie")
        }
    )
}
