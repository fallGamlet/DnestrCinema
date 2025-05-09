package com.fallgamlet.dnestrcinema.ui.images.viewer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ImageViewerScreen(
    state: ImageViewerVo,
    backAction: () -> Unit = { },
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        AsyncImage(
            model = state.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
        )

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

@Preview(name = "Light")
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewImageViewerScreen() {
    val state = ImageViewerVo(
        imageUrl = "http://kinotir.md/files/resize/1969.264x.jpg?db5b110fee8176f917deeb392e982494"
    )
    MaterialTheme(
        colorScheme = if(isSystemInDarkTheme()) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }
    ) {
        ImageViewerScreen(
            state = state,
            backAction = { },
        )
    }
}
