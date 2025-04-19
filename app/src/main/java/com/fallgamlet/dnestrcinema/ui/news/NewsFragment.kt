package com.fallgamlet.dnestrcinema.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fallgamlet.dnestrcinema.ui.base.BaseFragment
import com.fallgamlet.dnestrcinema.ui.news.composable.NewsListScreen
import kotlinx.coroutines.launch


class NewsFragment : BaseFragment() {

    private lateinit var viewModel: NewsViewModelImpl

    override val layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModelProvider().get(NewsViewModelImpl::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state = viewModel.dataState.collectAsState(NewsListScreenState())
                NewsListScreen(
                    newsList = state.value.items,
                    isRefreshing = state.value.isRefreshing,
                    onRefresh = { viewModel.loadData() },
                )
            }
            setOnClickListener { }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.errorsState.collect { error ->
                    onError(error)
                }
            }
        }
    }
}
