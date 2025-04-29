package com.fallgamlet.dnestrcinema.ui.movie.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fallgamlet.dnestrcinema.ui.movie.model.MovieVo
import com.fallgamlet.dnestrcinema.ui.movie.model.ScheduleItemVo


@Composable
fun MovieSnippet(
    movie: MovieVo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            model = movie.posterUtl,
            contentDescription = null,
            modifier = Modifier
                .width(64.dp)
                .height(96.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
        )
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = movie.pubDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            movie.schedules.forEach { scheduleItem ->
                Row {
                    Text(
                        text = scheduleItem.room,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 4.dp),
                    )
                    Text(
                        text = scheduleItem.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SoonMovieItemPreview() {
    val model = MovieVo(
        title = "Movie Title",
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

    Column(
        modifier = Modifier.background(color = Color.White)
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme()
        ) {
            MovieSnippet(
                movie = model,
                onClick = { }
            )
        }
        MaterialTheme(
            colorScheme = darkColorScheme()
        ) {
            MovieSnippet(
                movie = model,
                onClick = { }
            )
        }
    }
}
