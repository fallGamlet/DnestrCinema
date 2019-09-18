package com.fallgamlet.dnestrcinema.ui.news

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.ui.adapters.BaseRecyclerAdapter
import com.fallgamlet.dnestrcinema.ui.base.BaseFragment


class NewsFragment : BaseFragment() {

    @BindView(R.id.swipeLayout)
    protected lateinit var swipeLayout: SwipeRefreshLayout
    @BindView(R.id.listView)
    protected lateinit var listView: RecyclerView
    @BindView(R.id.placeholderView)
    protected lateinit var placeholderView: TextView

    private lateinit var viewModel: NewsViewModelImpl
    private lateinit var listAdapter: NewsRecyclerAdapter


    override val layoutId: Int = R.layout.fragment_movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModelProvider().get(NewsViewModelImpl::class.java)

        val viewState = viewModel.viewState
        setErrorLive(viewState.error)
        setLoadingLive(viewState.loading)

        viewState.items.observe(this, Observer { onItemsChanged(it) })
    }

    private fun onItemsChanged(items: List<NewsItem>) {
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

        listAdapter = NewsRecyclerAdapter()

        listView.adapter = listAdapter
        listView.layoutManager = LinearLayoutManager(context)
        listView.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        listView.addItemDecoration(SpacerItemDecoration().apply {
            val space = resources.getDimension(R.dimen.SpaceMiddle).toInt()
            setSpace(space)
        })
    }
}
