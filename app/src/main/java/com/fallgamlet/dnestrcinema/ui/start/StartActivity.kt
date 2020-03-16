package com.fallgamlet.dnestrcinema.ui.start

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade.Companion.instance
import com.fallgamlet.dnestrcinema.data.localstore.AccountLocalRepository
import com.fallgamlet.dnestrcinema.domain.models.AccountItem
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView
import com.fallgamlet.dnestrcinema.ui.base.BaseActivity
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailActivity
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailActivity.Companion.ARG_MOVIE
import com.fallgamlet.dnestrcinema.ui.navigation.MvpBottomNavigationView
import com.fallgamlet.dnestrcinema.ui.navigation.MvpNavigationPresenterImpl
import com.fallgamlet.dnestrcinema.utils.HttpUtils.getAbsoluteUrl
import com.fallgamlet.dnestrcinema.utils.ViewUtils.shareApp
import com.fallgamlet.dnestrcinema.utils.reactive.ObserverUtils.emptyDisposableObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StartActivity : BaseActivity(), NavigationRouter {
    @BindView(R.id.viewpager)
    protected lateinit var mViewPager: ViewPager
    @BindView(R.id.bottomNavigationView)
    protected lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var bottomNavigationPresenter: MvpNavigationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        ButterKnife.bind(this)
        initData()
    }

    private fun initData() {
        initAccount()
        initNavigation()
        setupViewPager(mViewPager)
        showToday()
    }

    private fun initAccount() {
        AccountLocalRepository(this).items
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { value: List<AccountItem> ->
                initAccount(value)
                true
            }
            .onErrorReturnItem(false)
            .subscribe(emptyDisposableObserver())
    }

    private fun initAccount(items: List<AccountItem>) {
        val cinemaId = instance.cinemaItem!!.id
        val accountItem = items
            .firstOrNull { it.cinemaId == cinemaId }
            ?: return

        instance.accountItem = accountItem
        instance.netClient?.apply {
            login = accountItem.login
            password = accountItem.password
        }
    }

    private fun initNavigation() {
        val bottomNavigationView: MvpNavigationView = MvpBottomNavigationView(mBottomNavigationView)
        bottomNavigationPresenter = MvpNavigationPresenterImpl(bottomNavigationView, this)
        bottomNavigationView.presenter = bottomNavigationPresenter
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        viewPager!!.addOnPageChangeListener(onPageChangeListener)
        viewPager.adapter = pageAdapter
    }

    @get:Synchronized
    private val pageAdapter: ViewPagerAdapter
        get() = adapter

    private fun addFragment(navigationId: Int) {
        val config = instance
        val navigationItem = config.navigationCreator?.getNavigationItem(navigationId) ?: return
        val fragment = createFragment(navigationId) ?: return
        val title = getString(navigationItem.titleId)
        adapter.addFragment(fragment, title)
    }

    private fun createFragment(navigationId: Int): Fragment? {
        return when (navigationId) {
            NavigationItem.NavigationId.TODAY -> instance.fragmentFactory!!.createTodayView()
            NavigationItem.NavigationId.SOON -> instance.fragmentFactory!!.createSoonView()
            NavigationItem.NavigationId.TICKETS -> instance.fragmentFactory!!.createTicketsView()
            NavigationItem.NavigationId.NEWS -> instance.fragmentFactory!!.createNewsView()
            NavigationItem.NavigationId.ABOUT -> instance.fragmentFactory!!.createAboutView()
            else -> null
        }
    }

    private val onPageChangeListener: OnPageChangeListener
        get() = object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                this@StartActivity.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }

    private fun onPageSelected(position: Int) {
        when (instance.navigations?.getOrNull(position)) {
            NavigationItem.NavigationId.TODAY -> showToday()
            NavigationItem.NavigationId.SOON -> showSoon()
            NavigationItem.NavigationId.TICKETS -> showTickets()
            NavigationItem.NavigationId.NEWS -> showNews()
            NavigationItem.NavigationId.ABOUT -> showAbout()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.actionShareApp -> {
                shareApp(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        instance.navigationRouter = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        instance.navigationRouter = this
    }

    override fun showToday() {
        showViewWithNavigationId(NavigationItem.NavigationId.TODAY)
        bottomNavigationPresenter.onTodaySelected()
    }

    override fun showSoon() {
        showViewWithNavigationId(NavigationItem.NavigationId.SOON)
        bottomNavigationPresenter.onSoonSelected()
    }

    override fun showTickets() {
        showViewWithNavigationId(NavigationItem.NavigationId.TICKETS)
        bottomNavigationPresenter.onTicketsSelected()
    }

    override fun showAbout() {
        showViewWithNavigationId(NavigationItem.NavigationId.ABOUT)
        bottomNavigationPresenter.onAboutSelected()
    }

    override fun showNews() {
        showViewWithNavigationId(NavigationItem.NavigationId.NEWS)
        bottomNavigationPresenter.onNewsSelected()
    }

    private fun showViewWithNavigationId(navigationId: Int) {
        val pos = instance.navigations!!.indexOf(navigationId)
        showViewWithPosition(pos)
    }

    private fun showViewWithPosition(position: Int) {
        if (position < 0 || position >= mViewPager.adapter!!.count) {
            return
        }
        if (mViewPager.currentItem != position) {
            mViewPager.currentItem = position
        }
    }

    override fun showMovieDetail(movieItem: MovieItem?) {
        movieItem ?: return
        val bundle = Bundle().apply { putParcelable(ARG_MOVIE, movieItem) }
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun showBuyTicket(movieItem: MovieItem?) {
        movieItem ?: return
        val baseUrl = instance.requestFactory!!.baseUrl
        val url = getAbsoluteUrl(baseUrl, movieItem.buyTicketLink) ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
