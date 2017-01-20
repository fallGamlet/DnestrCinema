package com.fallgamlet.dnestrcinema;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fallgamlet.dnestrcinema.dialogs.MessageDialog;
import com.fallgamlet.dnestrcinema.network.DataSettings;
import com.fallgamlet.dnestrcinema.network.Network;
import com.fallgamlet.dnestrcinema.network.RssItem;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        if (item == null) { return false; }

        int id = item.getItemId();
        boolean res = false;
        if (id == R.id.actionRefresh) {
            res = true;
            loadRss();
        } else {
            res = super.onOptionsItemSelected(item);
        }
        return res;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("rss", getRssList());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ArrayList<RssItem> list = savedInstanceState.getParcelableArrayList("rss");
        getRssList().clear();
        if (list != null) { getRssList().addAll(list); }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        if (getRssList().isEmpty()) {
            loadRss();
        } else {
            updateData(getRssList());
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cinemaNowFragment = null;
        cinemaSoonFragment = null;
        aboutFragment = null;
        mToolbar = null;
        mTablayout = null;
        mViewPager = null;
    }
    //endregion

    //region Methods
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        adapter.addFragment(getCinemaNowFragment(), getString(R.string.title_now));
        adapter.addFragment(getCinemaSoonFragment(), getString(R.string.title_soon));
        adapter.addFragment(getAboutFragment(), getString(R.string.title_about));

        adapter.notifyDataSetChanged();
    }

    private void loadRss() {
        Network.RequestData requestData = new Network.RequestData();
        requestData.options = Network.Options.Default();
        requestData.options.contentType = Network.CONTENT_TYPE_XML;
        requestData.url = DataSettings.BASE_URL + DataSettings.PATH_RSS;

        getMessageDialog().showWaiting(null);
        Network.requestDataAsync(requestData, new Network.ResponseHandle() {
            @Override
            public void finished(Exception exception, Network.StrResult result) {
                if (exception != null) {
                    String msg=null;
                    if (exception instanceof UnknownHostException) {
                        msg = "Веб сервис недоступен. Возможно нет соединение с сетью Интернет";
                    } else {
                        msg = exception.toString();
                    }

                    getMessageDialog().showMessage(getString(R.string.error), msg);
                    pushToLog(exception.toString());
                    return;
                }

                if (result.error != null) {
                    getMessageDialog().showMessage(result.error);
                    pushToLog(result.error);
                    return;
                }

                ArrayList<RssItem> rssItems = null;
                try {
                    rssItems = RssItem.parseRSS(result.data);
                } catch (Exception e) {
                    pushToLog(e.toString());

                    String msg = e.toString();
                    getMessageDialog().showMessage(getString(R.string.error), msg);
                }
                dismissMessageDialog();

                getRssList().clear();
                if (rssItems != null) { getRssList().addAll(rssItems); }

                updateData(rssItems);
            }
        });
    }

    protected void updateData(@Nullable List<RssItem> itemList) {
        Date now = new Date();

        if (itemList != null) {
            Collections.sort(itemList, RssItem.getDateTitleComparator());
        }

        ArrayList<RssItem> nowItems = getCinemaNowFragment().getDataItems();
        ArrayList<RssItem> soonItems = getCinemaSoonFragment().getDataItems();

        nowItems.clear();
        soonItems.clear();

        if (itemList != null) {
            for (RssItem item: itemList) {
                Date pubDate = item.getPubDate();
                if (pubDate == null || pubDate.after(now)) {
                    soonItems.add(item);
                } else {
                    nowItems.add(item);
                }
            }
        }

        getCinemaNowFragment().notifyDataSetChanged();
        getCinemaSoonFragment().notifyDataSetChanged();
    }
    //endregion

    //region Static Methods
    public static void hideKeyboard(Activity activity, View v) {
        if (v == null) { v = activity.getWindow().getCurrentFocus(); }
        if (v == null) { return; }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(@NonNull View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public static  void pushToLog(String txt) {
        pushToLog("MyLog", txt);
    }

    public static  void pushToLog(String tag, String txt) {
        Log.d(tag, txt);
    }
    //endregion

    //region Fragments singletons
    protected CinemaFragment cinemaNowFragment;
    protected CinemaFragment getCinemaNowFragment() {
        if (cinemaNowFragment == null) {
            cinemaNowFragment = new CinemaFragment();
        }
        return cinemaNowFragment;
    }

    protected CinemaFragment cinemaSoonFragment;
    protected CinemaFragment getCinemaSoonFragment() {
        if (cinemaSoonFragment == null) {
            cinemaSoonFragment = new CinemaFragment();
        }
        return cinemaSoonFragment;
    }

    protected AboutFragment aboutFragment;
    protected AboutFragment getAboutFragment() {
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
        }
        return aboutFragment;
    }
    //endregion

    //region Dialog
    MessageDialog mMessageDialog;
    protected MessageDialog getMessageDialog() {
        if (mMessageDialog == null ) {
            mMessageDialog = MessageDialog.newInstance(getSupportFragmentManager(), null);
        }
        return mMessageDialog;
    }

    protected void dismissMessageDialog() {
        if (mMessageDialog != null) {
            mMessageDialog.dismiss();
            mMessageDialog = null;
        }
    }

    //endregion

    //region RssList singleton
    private ArrayList<RssItem> mRssList;
    protected ArrayList<RssItem> getRssList() {
        if (mRssList == null) {
            mRssList = new ArrayList<>(100);
        }
        return mRssList;
    }
    //endregion
}
