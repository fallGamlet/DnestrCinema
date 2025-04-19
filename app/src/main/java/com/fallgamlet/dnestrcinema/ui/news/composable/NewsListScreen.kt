package com.fallgamlet.dnestrcinema.ui.news.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fallgamlet.dnestrcinema.ui.news.NewsVo
import com.fallgamlet.dnestrcinema.ui.shared.SwipeToRefreshItems
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun NewsListScreen(
    newsList: List<NewsVo>,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit,
) {
    SwipeToRefreshItems(
        count = newsList.size,
        isRefreshing = isRefreshing,
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh,
        itemContent = { index ->
            NewsSnippet(
                news = newsList[index],
            )
        },
    )
}

@OptIn(DelicateCoroutinesApi::class)
@Preview
@Composable
private fun PreviewNewsListScreen() {
    val items = (0..9).map {
        NewsVo(
            id = UUID.randomUUID().toString(),
            tag = "",
            date = "17 July",
            title = "News title example",
            body = "Lorem ipsum example text lorem ipsum prolongation text. ".repeat(10),
            imgUrls = listOf("https://www.enigme-facile.fr/wp-content/uploads/2018/04/16578136336.jpg"),
        )
    }

    val itemsState = remember { mutableStateOf(items) }
    val isRefreshing = remember { mutableStateOf(false) }
    val refreshingCount = remember { mutableStateOf(0) }

    NewsListScreen(
        newsList = items,
        isRefreshing = isRefreshing.value,
        onRefresh = {
            GlobalScope.launch {
                isRefreshing.value = true
                refreshingCount.value++
                delay(1000)
                isRefreshing.value = false
                itemsState.value = itemsState.value.reversed()
            }
        },
    )
}
