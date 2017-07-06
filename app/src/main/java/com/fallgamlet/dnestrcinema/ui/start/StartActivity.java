package com.fallgamlet.dnestrcinema.ui.start;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.ui.movie.today.CinemaFragment;
import com.fallgamlet.dnestrcinema.ui.movie.soon.CinemaFragmentSoon;
import com.fallgamlet.dnestrcinema.ui.about.AboutFragment;
import com.fallgamlet.dnestrcinema.ui.news.NewsFragment;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.utils.ViewUtils;

public class StartActivity
        extends AppCompatActivity
        implements NavigationRouter
{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //region Fields
    private Toolbar mToolbar;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    //endregion

    //region Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setOffscreenPageLimit(0);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }

        setupViewPager(mViewPager);
        mTablayout.setupWithViewPager(mViewPager);
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
        cinemaNowFragment = null;
        cinemaSoonFragment = null;
        aboutFragment = null;
        mToolbar = null;
        mTablayout = null;
        mViewPager = null;

        super.onDestroy();
    }
    //endregion

    //region Methods
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        adapter.addFragment(getCinemaNowFragment(), getString(R.string.now));
        adapter.addFragment(getCinemaSoonFragment(), getString(R.string.soon));
        adapter.addFragment(getCinemaNewsFragment(), getString(R.string.news));
        adapter.addFragment(getAboutFragment(), getString(R.string.about));

        adapter.notifyDataSetChanged();
    }
    //endregion

    //region Fragments singletons
    protected CinemaFragment cinemaNowFragment;
    protected CinemaFragment getCinemaNowFragment() {
        if (cinemaNowFragment == null) {
            cinemaNowFragment = CinemaFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_NOW);
        }
        return cinemaNowFragment;
    }

    protected CinemaFragmentSoon cinemaSoonFragment;
    protected CinemaFragmentSoon getCinemaSoonFragment() {
        if (cinemaSoonFragment == null) {
            cinemaSoonFragment = CinemaFragmentSoon.newInstance(KinoTir.BASE_URL+KinoTir.PATH_SOON);
        }
        return cinemaSoonFragment;
    }

    protected NewsFragment cinemaNewsFragment;
    protected NewsFragment getCinemaNewsFragment() {
        if (cinemaNewsFragment == null) {
            cinemaNewsFragment = NewsFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_NEWS);
        }
        return cinemaNewsFragment;
    }

    protected AboutFragment aboutFragment;
    protected AboutFragment getAboutFragment() {
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
        }
        return aboutFragment;
    }
    //endregion

    @Override
    public void showToday() {

    }

    @Override
    public void showSoon() {

    }

    @Override
    public void showTickets() {

    }

    @Override
    public void showAbout() {

    }

    @Override
    public void showNews() {

    }
}
