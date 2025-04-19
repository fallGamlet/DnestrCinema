package com.fallgamlet.dnestrcinema.ui.news.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fallgamlet.dnestrcinema.ui.news.NewsVo

@Composable
fun NewsSnippet(
    news: NewsVo,
) {
    Card(
        shape = CardDefaults.shape,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                val imageUrl = news.imgUrls.firstOrNull() ?: ""
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = news.imgUrls.firstOrNull(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
                Text(
                    text = news.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.wrapContentSize()
                        .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .align(alignment = Alignment.BottomEnd)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 4.dp
                        )
                )
            }
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),

            )
            Text(
                text = news.body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                )
        }
    }
}

@Preview
@Composable
private fun NewsSnippetPreview() {
    val model = NewsVo(
        id = "123",
        tag = "",
        title = "News Title with long text for using two lines for test text font and space betwean lines",
        date = "17 July",
        body = "Lorem ipsum example text lorem ipsum. ".repeat(10),
        imgUrls = listOf("https://www.enigme-facile.fr/wp-content/uploads/2018/04/16578136336.jpg")
    )

    Column(
        modifier = Modifier.background(color = Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme()
        ) {
            NewsSnippet(
                news = model,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        MaterialTheme(
            colorScheme = darkColorScheme()
        ) {
            NewsSnippet(
                news = model,
            )
        }
    }
}
