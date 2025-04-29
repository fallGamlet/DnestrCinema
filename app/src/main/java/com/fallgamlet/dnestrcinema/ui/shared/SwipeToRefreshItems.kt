package com.fallgamlet.dnestrcinema.ui.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.fallgamlet.dnestrcinema.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToRefreshItems(
    count: Int,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    key: ((index: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
    onRefresh: () -> Unit,
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                state = state,
            )
        },
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(
                count = count,
                key = key,
                contentType = contentType,
            ) { index ->
                itemContent(index)
            }
        }
        if (count == 0 && !isRefreshing) {
            Text(
                text = stringResource(R.string.yet_empty),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
