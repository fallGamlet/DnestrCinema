@file:OptIn(ExperimentalMaterial3Api::class)

package com.fallgamlet.dnestrcinema.ui.movie.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fallgamlet.dnestrcinema.R

@Composable
fun MovieDetailsScreen(
    movie: MovieDetailsVo,
    labels: MovieDetailsLabels,
    trailerAction: ((String) -> Unit)? = null,
    backAction: () -> Unit = { },
) {
    val config = LocalConfiguration.current
    val spaceTop = config.screenWidthDp.times(1.48f).minus(16)

    val initialScroll = (0.5f * spaceTop * LocalDensity.current.density).toInt()
    val scrollState = rememberScrollState(initialScroll)

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                ),
        ) {
            Spacer(
                modifier = Modifier
                    .height(spaceTop.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge.copy(
                    bottomStart = CornerSize(0f),
                    bottomEnd = CornerSize(0f),
                ),
                colors = CardDefaults.elevatedCardColors(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                MovieInfo(
                    movie = movie,
                    labels = labels,
                    trailerAction = trailerAction,
                )
            }
        }

        IconButton(
            onClick = backAction,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
            )
        }
    }
}

@Composable

private fun ColumnScope.MovieInfo(
    movie: MovieDetailsVo,
    labels: MovieDetailsLabels,
    trailerAction: ((String) -> Unit)? = null,
) {
    val spaceMedium = 16.dp
    val spaceSmall = 8.dp

    Text(
        text = movie.title,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(spaceMedium)
    )

    movie.trailerMovieUrls.firstOrNull()?.also { trailerUrl ->
        Button(
            onClick = { trailerAction?.invoke(trailerUrl) },
            modifier = Modifier
                .padding(horizontal = spaceMedium)
                .align(Alignment.CenterHorizontally),
        ) {
            Text(text = labels.trailer)
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(start = spaceSmall),
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = spaceMedium)
            .padding(bottom = spaceSmall, top = spaceSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = movie.pubDate,
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = movie.ageLimit,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    val scheduleItemModifier = Modifier
        .padding(horizontal = spaceMedium)
        .padding(bottom = spaceSmall)
    movie.schedule.forEach { item ->
        Text(
            text = item,
            style = MaterialTheme.typography.bodyLarge,
            modifier = scheduleItemModifier
        )
    }

    val labelValueModifier = Modifier.fillMaxWidth()
        .padding(horizontal = spaceMedium)
        .padding(top = spaceSmall)

    LabelAndValue(labels.duration, movie.duration, labelValueModifier)
    LabelAndValue(labels.country, movie.country, labelValueModifier)
    LabelAndValue(labels.scenario, movie.scenario, labelValueModifier)
    LabelAndValue(labels.actors, movie.actors, labelValueModifier)
    LabelAndValue(labels.genre, movie.genre, labelValueModifier)
    LabelAndValue(labels.budget, movie.budget, labelValueModifier)

    Text(
        text = movie.description,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify,
        modifier = Modifier.fillMaxWidth()
            .padding(all = spaceMedium),
    )
}

@Composable
private fun LabelAndValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    if (value.isBlank()) return

    Row(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(end = 4.dp)
                .alignByBaseline()
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .alignByBaseline()
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewMovieDetailsScreen() {
    val movie = MovieDetailsVo(
        title = "Movie Title Text",
        pubDate = "April 25",
        schedule = listOf(
            "Blue room: 10:30, 14:00, 18:45",
            "DVD room: 11:20, 15:20, 19:15",
        ),
        duration = "110 minutes",
        director = "Bruce Ainhorn",
        actors = "John Smith, Jack Joyinsten, Jany Hawk",
        scenario = "John Smith",
        ageLimit = "12+",
        budget = "1 000 000 $",
        country = "Australia, England, Denmark",
        genre = "Thriller",
        description = "Some movie description as Lorum Ipsum with longer and longer text. "
            .repeat(5),
        posterUrl = "http://kinotir.md/files/resize/1969.264x.jpg?db5b110fee8176f917deeb392e982494",
        imageUrls = emptyList(),
        trailerMovieUrls = listOf("https://youtu.be/7jjilh6DBw8"),
    )
    MaterialTheme(
        colorScheme = if(isSystemInDarkTheme()) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }
    ) {
        MovieDetailsScreen(
            movie = movie,
            labels = MovieDetailsLabels(
                duration = stringResource(R.string.label_duration),
                director = stringResource(R.string.label_director),
                scenario = stringResource(R.string.label_scenario),
                actors = stringResource(R.string.label_actors),
                budget = stringResource(R.string.label_budget),
                ageLimit = stringResource(R.string.label_agelimit),
                country = stringResource(R.string.label_country),
                genre = stringResource(R.string.label_genre),
                trailer = stringResource(R.string.label_trailer),
            )
        )
    }
}
