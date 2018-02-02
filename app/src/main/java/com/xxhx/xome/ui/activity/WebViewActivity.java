package com.xxhx.xome.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.xxhx.xome.R;
import org.xwalk.core.XWalkView;

public class WebViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_URL = "extra_url";

    //private SwipeRefreshLayout mRefreshView;
    private WebView mWebView;
    private XWalkView xWalkView;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mUrl = getIntent().getStringExtra(EXTRA_URL);

        //mRefreshView = (SwipeRefreshLayout) findViewById(R.id.refresh);
        //mRefreshView.setColorSchemeResources(R.color.home);
        //mRefreshView.setOnRefreshListener(this);
        mWebView = (WebView) findViewById(R.id.web);
        xWalkView = (XWalkView) findViewById(R.id.xwalk);
        initWeb();
        mWebView.loadUrl(mUrl);
        //xWalkView.loadUrl(mUrl);
    }

    public boolean isSharedMode() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    private void initWeb() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                //if(mRefreshView.isRefreshing()) {
                //    mRefreshView.setRefreshing(false);
                //}
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    public void onRefresh() {
        mWebView.loadUrl(mUrl);
    }
}
