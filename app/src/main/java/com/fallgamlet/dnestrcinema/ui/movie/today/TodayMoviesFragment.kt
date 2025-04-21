package com.fallgamlet.dnestrcinema.ui.movie.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fallgamlet.dnestrcinema.dagger.getAppComponent
import com.fallgamlet.dnestrcinema.ui.movie.composable.MoviesComposable
import com.fallgamlet.dnestrcinema.ui.utils.showError
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodayMoviesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TodayMoviesViewModel by viewModels { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
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
                viewModel.errorsState
                    .filterNotNull()
                    .collect {
                        showError(it)
                    }
            }
        }
    }
}
