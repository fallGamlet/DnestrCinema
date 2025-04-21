package com.fallgamlet.dnestrcinema.ui.start

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade.Companion.instance
import com.fallgamlet.dnestrcinema.dagger.getAppComponent
import com.fallgamlet.dnestrcinema.data.localstore.AccountLocalRepository
import com.fallgamlet.dnestrcinema.domain.models.AccountItem
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView
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

class StartActivity : AppCompatActivity(), NavigationRouter {
    private lateinit var mViewPager: ViewPager2
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var bottomNavigationPresenter: MvpNavigationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        mViewPager = findViewById(R.id.viewpager)
        mBottomNavigationView = findViewById(R.id.bottomNavigationView)
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

    private fun setupViewPager(viewPager: ViewPager2) {
        initViewPagerAdapter()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handlePageSelection(position)
            }
        })
        viewPager.adapter = adapter
    }

    private fun initViewPagerAdapter() {
        adapter = ViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            getCount = { instance.navigations.size },
            createPage = ::createFragment
        )
    }

    private fun createFragment(position: Int): Fragment {
        val fragmentFactory = instance.fragmentFactory
            ?: throw IllegalArgumentException("fragment factory must be initialized")
        val id = instance.navigations.getOrNull(position)
        return when (id) {
            NavigationItem.NavigationId.TODAY -> fragmentFactory.createTodayView()
            NavigationItem.NavigationId.SOON -> fragmentFactory.createSoonView()
            NavigationItem.NavigationId.NEWS -> fragmentFactory.createNewsView()
            NavigationItem.NavigationId.ABOUT -> fragmentFactory.createAboutView()
            else -> throw IllegalArgumentException("Not implemented page (position is $position, id is $id)")
        }
    }

    private fun handlePageSelection(position: Int) {
        val id = instance.navigations.getOrNull(position)
        when (id) {
            NavigationItem.NavigationId.TODAY -> showToday()
            NavigationItem.NavigationId.SOON -> showSoon()
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
        if (position < 0 || position >= adapter.itemCount)
            return

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
