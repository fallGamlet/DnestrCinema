package com.fallgamlet.dnestrcinema.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.fallgamlet.dnestrcinema.R;


public class CinemaBuyTicketActivity extends AppCompatActivity
{
    //region Static fields
    public static String ARG_URL = "url";
    public static String ARG_TITLE = "title";
    //endregion

    //region Fields
    private String url;
    private String title;

    Toolbar mToolbar;
    WebView mWebView;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        mToolbar = findViewById(R.id.toolbar);
        mWebView = findViewById(R.id.webView);
        initViews();

        Bundle bundle = getIntent().getExtras();
        initData(bundle);
    }

    private void initData(Bundle bundle) {
        if (bundle != null) {
            url = bundle.getString(ARG_URL);
            title = bundle.getString(ARG_TITLE);
        }

        initData();
    }

    private void initData() {
        setTitle(title);
        mWebView.loadUrl(url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mWebView.saveState(outState);

        outState.putString(ARG_URL, url);
        outState.putString(ARG_URL, title);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mWebView.restoreState(savedInstanceState);

        if (savedInstanceState != null) {
            url = savedInstanceState.getString(ARG_URL);
            title = savedInstanceState.getString(ARG_TITLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean res = false;
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        mWebView.setNetworkAvailable(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mToolbar.setTitle(title);
    }

    //endregion
}
