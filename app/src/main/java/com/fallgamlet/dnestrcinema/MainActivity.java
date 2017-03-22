package com.fallgamlet.dnestrcinema;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.fallgamlet.dnestrcinema.network.KinoTir;
import com.fallgamlet.dnestrcinema.network.Network;
import com.fallgamlet.dnestrcinema.network.MovieItem;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        if (id == R.id.actionShareApp) {
            res = true;
            shareApp();
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

    private void shareApp() {
        try {
            String appID = getString(R.string.app_id);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.app_name)+" https://play.google.com/store/apps/details?id="+appID);
            sendIntent.setType("text/plain");;
            startActivity(Intent.createChooser(sendIntent, "Поделиться"));
        } catch (Exception e) {
            Log.d("Share", "Error: "+e.toString());
        }
    }

    private void writeStorage() {
        if (getRssList().isEmpty()) {
            return;
        }

        String filename = "data.json";
        FileOutputStream outStream;

        try {
            String data = MovieItem.toJSONArray(getRssList()).toString();
            outStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outStream.write(data.getBytes(Network.CHARSET_UTF8));
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readStorage() {
        String filename = "data.json";
        FileInputStream inStream;

        ArrayList<MovieItem> items = null;

        try {
            inStream = openFileInput(filename);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8*1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                byteStream.write(buffer, 0 ,len);
            }
            byte[] data = byteStream.toByteArray();
            String dataStr = new String(data, Network.CHARSET_UTF8);
            JSONArray jarr = new JSONArray(dataStr);
            items = MovieItem.setJSONArray(jarr);

            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            cinemaNowFragment = CinemaFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_NOW);
        }
        return cinemaNowFragment;
    }

    protected CinemaFragment cinemaSoonFragment;
    protected CinemaFragment getCinemaSoonFragment() {
        if (cinemaSoonFragment == null) {
            cinemaSoonFragment = CinemaFragment.newInstance(KinoTir.BASE_URL+KinoTir.PATH_SOON);
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
    private ArrayList<MovieItem> mRssList;
    protected ArrayList<MovieItem> getRssList() {
        if (mRssList == null) {
            mRssList = new ArrayList<>(100);
        }
        return mRssList;
    }
    //endregion
}
