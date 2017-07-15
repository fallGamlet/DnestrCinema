package com.fallgamlet.dnestrcinema.ui.start;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.factory.KinotirConfigFactory;
import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpAboutView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;
import com.fallgamlet.dnestrcinema.ui.navigation.MvpBottomNavigationView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.ui.navigation.MvpNavigationPresenterImpl;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketsView;
import com.fallgamlet.dnestrcinema.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity
        extends AppCompatActivity
        implements NavigationRouter
{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //region Fields
    @BindView(R.id.tablayout)
    protected TabLayout mTablayout;
    @BindView(R.id.viewpager)
    protected ViewPager mViewPager;
    @BindView(R.id.bottomNavigationView)
    protected BottomNavigationView mBottomNavigationView;

    private ViewPagerAdapter adapter;

    private NodeContainer nodeContainer;
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
        Config.getInstance().init(new KinotirConfigFactory());

        nodeContainer = new NodeContainer();

//        mBottomNavigationView.getMenu().clear();
//        mBottomNavigationView.inflateMenu(R.menu.menu_main);
//        mBottomNavigationView.getMenu().add()


        initNavigation();

        setupViewPager(mViewPager);

        showToday();
    }

    private void initNavigation() {
        MvpNavigationView bottomNavigationView = new MvpBottomNavigationView(mBottomNavigationView);
        bottomNavigationPresenter = new MvpNavigationPresenterImpl(bottomNavigationView, this);
        bottomNavigationView.setPresenter(bottomNavigationPresenter);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(getOnPageChangeListener());

        mTablayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(getPageAdapter());
    }

    private synchronized ViewPagerAdapter getPageAdapter() {
        if (adapter == null) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFragment(nodeContainer.getTodayFactory().getFragment(), getString(R.string.today));
            adapter.addFragment(nodeContainer.getSoonFactory().getFragment(), getString(R.string.soon));
            adapter.addFragment(nodeContainer.getLoginFactory().getFragment(), getString(R.string.tickets));
//            adapter.addFragment(nodeContainer.getTicketsFactory().getFragment(), getString(R.string.tickets));
            adapter.addFragment(nodeContainer.getNewsFactory().getFragment(), getString(R.string.news));
            adapter.addFragment(nodeContainer.getAboutFactory().getFragment(), getString(R.string.about));

            adapter.notifyDataSetChanged();
        }

        return adapter;
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
        Fragment fragment = adapter.getItem(position);

        if (fragment == null) {
            return;
        }

        if (fragment instanceof MvpTodayView) {
            showToday();
        }
        else if (fragment instanceof MvpSoonView) {
            showSoon();
        }
        else if (fragment instanceof MvpTicketsView) {
            showTickets();
        }
        else if (fragment instanceof MvpNewsView) {
            showNews();
        }
        else if (fragment instanceof MvpAboutView) {
            showAbout();
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
            ViewUtils.shareApp(this);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //region Fragments singletons
//    protected TodayMoviesFragment cinemaNowFragment;
//    protected TodayMoviesFragment getCinemaNowFragment() {
//        if (cinemaNowFragment == null) {
//            cinemaNowFragment = TodayMoviesFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_NOW);
//        }
//        return cinemaNowFragment;
//    }
//
//    protected SoonMoviesFragment cinemaSoonFragment;
//    protected SoonMoviesFragment getCinemaSoonFragment() {
//        if (cinemaSoonFragment == null) {
//            cinemaSoonFragment = SoonMoviesFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_SOON);
//        }
//        return cinemaSoonFragment;
//    }
//
//    protected NewsFragment cinemaNewsFragment;
//    protected NewsFragment getCinemaNewsFragment() {
//        if (cinemaNewsFragment == null) {
//            cinemaNewsFragment = NewsFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_NEWS);
//        }
//        return cinemaNewsFragment;
//    }
//
//    protected AboutFragment aboutFragment;
//    protected AboutFragment getAboutFragment() {
//        if (aboutFragment == null) {
//            aboutFragment = new AboutFragment();
//        }
//        return aboutFragment;
//    }
    //endregion

    @Override
    public void showToday() {
        int position = adapter.getPosition(nodeContainer.getTodayFactory().getFragment());

        showView(position);

        bottomNavigationPresenter.onTodaySelected();
    }

    @Override
    public void showSoon() {
        int position = adapter.getPosition(nodeContainer.getSoonFactory().getFragment());

        showView(position);

        bottomNavigationPresenter.onSoonSelected();
    }

    @Override
    public void showTickets() {
        int position = adapter.getPosition(nodeContainer.getTicketsFactory().getFragment());

        showView(position);

        bottomNavigationPresenter.onTicketsSelected();
    }

    @Override
    public void showAbout() {
        int position = adapter.getPosition(nodeContainer.getAboutFactory().getFragment());

        showView(position);

        bottomNavigationPresenter.onAboutSelected();
    }

    @Override
    public void showNews() {
        int position = adapter.getPosition(nodeContainer.getNewsFactory().getFragment());

        showView(position);

        bottomNavigationPresenter.onNewsSelected();
    }

    private void showView(int position) {
        if (position < 0 || position >= mViewPager.getAdapter().getCount()) {
            return;
        }

        if (mViewPager.getCurrentItem() != position) {
            mViewPager.setCurrentItem(position);
        }
    }
}
