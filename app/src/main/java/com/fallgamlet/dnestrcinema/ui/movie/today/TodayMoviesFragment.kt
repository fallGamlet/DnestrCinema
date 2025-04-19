package com.fallgamlet.dnestrcinema.ui.movie.today

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
import com.fallgamlet.dnestrcinema.ui.movie.composable.MoviesComposable
import kotlinx.coroutines.launch

class TodayMoviesFragment : BaseFragment() {

    private lateinit var viewModel: TodayMoviesViewModelImpl

    override val layoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModelProvider().get(TodayMoviesViewModelImpl::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val movies = viewModel.moviesVoState.collectAsState(emptyList())
                val isRefreshing = viewModel.isRefreshingState.collectAsState(false)
                MoviesComposable(
                    movies = movies.value,
                    isRefreshing = isRefreshing.value,
                    onRefresh = {
                        viewModel.loadData()
                    },
                    onClick = {
                        viewModel.onMovieSelected(it.link)
                    }
                )
            }
            setOnClickListener {  }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.errorsState.collect {
                    onError(it)
                }
            }
        }
    }
}
