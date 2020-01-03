package com.fallgamlet.dnestrcinema.ui.movie.today

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.ui.base.BaseFragment
import com.fallgamlet.dnestrcinema.ui.movie.DividerItemDecoration
import com.fallgamlet.dnestrcinema.ui.movie.MovieRecyclerAdapter


class TodayMoviesFragment : BaseFragment() {

    @BindView(R.id.swipeLayout)
    protected lateinit var swipeLayout: SwipeRefreshLayout
    @BindView(R.id.listView)
    protected lateinit var listView: RecyclerView
    @BindView(R.id.placeholderView)
    protected lateinit var placeholderView: View

    private lateinit var viewModel: TodayMoviesViewModelImpl
    private lateinit var listAdapter: MovieRecyclerAdapter


    override val layoutId: Int = R.layout.fragment_movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModelProvider().get(TodayMoviesViewModelImpl::class.java)

        val viewState = viewModel.viewState
        setErrorLive(viewState.error)
        setLoadingLive(viewState.loading)

        viewState.movies.observe(this, Observer { onMovieListChanged(it) })
    }

    private fun onMovieListChanged(items: List<MovieItem>) {
        val hasData = items.isNotEmpty()
        placeholderView.isVisible = !hasData
        listView.isVisible = hasData

        listAdapter.setData(items)
        listAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        view.setOnClickListener { }

        initSwipeLayout()
        initListView()

        viewModel.refreshViewState()
    }

    private fun initSwipeLayout() {
        swipeLayout.setOnRefreshListener { viewModel.loadData() }
        swipeLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent
        )
    }

    private fun initListView() {
        val context = listView.context

        listAdapter = MovieRecyclerAdapter(R.layout.movie_item_now)
        listAdapter.setListener(object : MovieRecyclerAdapter.OnAdapterListener {
            override fun onItemPressed(item: MovieItem, pos: Int) {
                viewModel.onMovieSelected(item)
            }

            override fun onItemSchedulePressed(item: MovieItem, pos: Int) {}

            override fun onItemBuyTicketPressed(item: MovieItem, pos: Int) {
                viewModel.onTicketBuySelected(item)
            }
        })

        listView.adapter = listAdapter
        listView.layoutManager = LinearLayoutManager(context)
        listView.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        listView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST).apply {
            val left = resources.getDimension(R.dimen.ImageDividerLeft).toInt()
            setPaddingStart(left)
        })
    }

    override fun onLoading(value: Boolean?) {
        super.onLoading(value)
        swipeLayout.isRefreshing = value == true
    }
}
