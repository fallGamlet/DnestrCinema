package com.fallgamlet.dnestrcinema.ui.start;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.fallgamlet.dnestrcinema.utils.ObserverUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.factory.KinotirConfigFactory;
import com.fallgamlet.dnestrcinema.data.localstore.AccountLocalRepository;
import com.fallgamlet.dnestrcinema.domain.models.AccountItem;
import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;
import com.fallgamlet.dnestrcinema.data.network.NetClient;
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailActivity;
import com.fallgamlet.dnestrcinema.ui.navigation.MvpBottomNavigationView;
import com.fallgamlet.dnestrcinema.ui.navigation.MvpNavigationPresenterImpl;
import com.fallgamlet.dnestrcinema.utils.HttpUtils;
import com.fallgamlet.dnestrcinema.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StartActivity
        extends
            AppCompatActivity
        implements
            NavigationRouter
{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //region Fields
    @BindView(R.id.viewpager)
    protected ViewPager mViewPager;
    @BindView(R.id.bottomNavigationView)
    protected BottomNavigationView mBottomNavigationView;

    private ViewPagerAdapter adapter;
    private MvpNavigationPresenter bottomNavigationPresenter;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        initAccount();

        initNavigation();

        setupViewPager(mViewPager);

        showToday();
    }

    private void initAccount() {
        AccountLocalRepository repository;
        repository = new AccountLocalRepository(this);

        repository.getItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(value -> {
                    initAccount(value);
                    return true;
                })
                .onErrorReturnItem(false)
                .subscribe(ObserverUtils.emptyDisposableObserver());
    }

    private void initAccount(List<AccountItem> items) {
        long cinemaId = AppFacade.Companion.getInstance().getCinemaItem().getId();

        AccountItem accountItem = null;
        for (AccountItem item: items) {
            if (item.getCinemaId() == cinemaId) {
                accountItem = item;
                break;
            }
        }

        if (accountItem != null) {
            AppFacade.Companion.getInstance().setAccountItem(accountItem);

            NetClient netClient = AppFacade.Companion.getInstance().getNetClient();
            netClient.setLogin(accountItem.getLogin());
            netClient.setPassword(accountItem.getPassword());
        }
    }

    private void initNavigation() {
        MvpNavigationView bottomNavigationView = new MvpBottomNavigationView(mBottomNavigationView);
        bottomNavigationPresenter = new MvpNavigationPresenterImpl(bottomNavigationView, this);
        bottomNavigationView.setPresenter(bottomNavigationPresenter);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(getOnPageChangeListener());
        viewPager.setAdapter(getPageAdapter());
    }

    private synchronized ViewPagerAdapter getPageAdapter() {
        if (adapter == null) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());;

            for (Integer navId: AppFacade.Companion.getInstance().getNavigations()) {
                addFragment(navId);
            }

            adapter.notifyDataSetChanged();
        }

        return adapter;
    }

    private void addFragment(Integer navigationId) {
        AppFacade config = AppFacade.Companion.getInstance();
        NavigationItem navigationItem = config.getNavigationCreator()
                                            .getNavigationItem(navigationId);

        Fragment fragment = createFragment(navigationId);

        if (navigationItem == null || fragment == null) {
            return;
        }

        String title = getString(navigationItem.getTitleId());
        adapter.addFragment(fragment, title);
    }

    private Fragment createFragment(int navigationId) {
        Fragment fragment;

        switch (navigationId) {
            case NavigationItem.NavigationId.TODAY:
                fragment = AppFacade.Companion.getInstance().getFragmentFactory().createTodayView();
                break;
            case NavigationItem.NavigationId.SOON:
                fragment = AppFacade.Companion.getInstance().getFragmentFactory().createSoonView();
                break;
            case NavigationItem.NavigationId.TICKETS:
                fragment = AppFacade.Companion.getInstance().getFragmentFactory().createTicketsView();
                break;
            case NavigationItem.NavigationId.NEWS:
                fragment = AppFacade.Companion.getInstance().getFragmentFactory().createNewsView();
                break;
            case NavigationItem.NavigationId.ABOUT:
                fragment = AppFacade.Companion.getInstance().getFragmentFactory().createAboutView();
                break;
            default:
                fragment = null;
                break;
        }

        return fragment;
    }


    private ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                StartActivity.this.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void onPageSelected(int position) {
        List<Integer> navigations = AppFacade.Companion.getInstance().getNavigations();
        if (position < 0 || position >= navigations.size()) {
            return;
        }

        int navId = navigations.get(position);
        switch (navId) {
            case NavigationItem.NavigationId.TODAY:
                showToday();
                break;
            case NavigationItem.NavigationId.SOON:
                showSoon();
                break;
            case NavigationItem.NavigationId.TICKETS:
                showTickets();
                break;
            case NavigationItem.NavigationId.NEWS:
                showNews();
                break;
            case NavigationItem.NavigationId.ABOUT:
                showAbout();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null) {
            return false;
        }

        int id = item.getItemId();
        boolean res = false;

        if (id == R.id.actionShareApp) {
            ViewUtils.INSTANCE.shareApp(this);
            res = true;
        } else {
            res = super.onOptionsItemSelected(item);
        }
        return res;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        AppFacade.Companion.getInstance().setNavigationRouter(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppFacade.Companion.getInstance().setNavigationRouter(this);
    }

    @Override
    public void showToday() {
        showViewWithNavigationId(NavigationItem.NavigationId.TODAY);
        bottomNavigationPresenter.onTodaySelected();
    }

    @Override
    public void showSoon() {
        showViewWithNavigationId(NavigationItem.NavigationId.SOON);
        bottomNavigationPresenter.onSoonSelected();
    }

    @Override
    public void showTickets() {
        showViewWithNavigationId(NavigationItem.NavigationId.TICKETS);
        bottomNavigationPresenter.onTicketsSelected();
    }

    @Override
    public void showAbout() {
        showViewWithNavigationId(NavigationItem.NavigationId.ABOUT);
        bottomNavigationPresenter.onAboutSelected();
    }

    @Override
    public void showNews() {
        showViewWithNavigationId(NavigationItem.NavigationId.NEWS);
        bottomNavigationPresenter.onNewsSelected();
    }

    private void showViewWithNavigationId(int navigationId) {
        int pos = AppFacade.Companion.getInstance().getNavigations().indexOf(navigationId);
        showViewWithPosition(pos);
    }

    private void showViewWithPosition(int position) {
        if (position < 0 || position >= mViewPager.getAdapter().getCount()) {
            return;
        }

        if (mViewPager.getCurrentItem() != position) {
            mViewPager.setCurrentItem(position);
        }
    }

    @Override
    public void showMovieDetail(MovieItem movieItem) {
        if (movieItem != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailActivity.ARG_MOVIE, movieItem);

            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void showBuyTicket(MovieItem movieItem) {
        if (movieItem == null) {
            return;
        }

        String baseUrl = AppFacade.Companion.getInstance().getRequestFactory().getBaseUrl();
        String path = movieItem.getBuyTicketLink();
        String url = HttpUtils.INSTANCE.getAbsoluteUrl(baseUrl, path);

        if (url == null) {
//            ViewUtils.showToast(this, "");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
