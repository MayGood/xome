package com.xxhx.xome.ui.home.signin;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Constants;
import com.xxhx.xome.helper.PreferHelper;
import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.WeiboService;
import com.xxhx.xome.manager.WeiboManager;
import com.xxhx.xome.ui.BaseActivity;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SigninActivity extends BaseActivity {
    private static final String sWeiboAuthorizeUrl = "https://api.weibo.com/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s";

    private WebView mWebView;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_signin);

        mWebView = (WebView) findViewById(R.id.web);
        initWebView();
        //mWebView.loadUrl("file:///android_asset/signin.html");
        mWebView.loadUrl(String.format(sWeiboAuthorizeUrl, Constants.sWeiboClientId, Constants.sWeiboRedirectUri));
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith(Constants.sWeiboRedirectUri)) {
                    int start = url.indexOf("code=");
                    String code = url.substring(start + 5);
                    WeiboManager.getInstance().authAccessToken(new Subscriber<AccessToken>() {
                        @Override
                        public void onCompleted() {
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(SigninActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(AccessToken accessToken) {
                            PreferHelper.setWeiboAccessToken(accessToken);
                            if(accessToken.isValid()) {
                                WeiboManager.getInstance().setAccessToken(accessToken);
                            }
                        }
                    }, code);
                    return true;
                    //Retrofit retrofit = new Retrofit.Builder()
                    //        .baseUrl("https://api.weibo.com/oauth2/")
                    //        .addConverterFactory(GsonConverterFactory.create())
                    //        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //        .build();
                    //WeiboService weiboService = retrofit.create(WeiboService.class);
                    //weiboService.accessToken(Constants.sWeiboClientId, Constants.sWeiboClientSecret,
                    //        "authorization_code", Constants.sWeiboRedirectUri, code)
                    //        .subscribeOn(Schedulers.io())
                    //        .observeOn(AndroidSchedulers.mainThread())
                    //        .subscribe(new Subscriber<AccessToken>() {
                    //            @Override
                    //            public void onCompleted() {
                    //            }
                    //
                    //            @Override
                    //            public void onError(Throwable e) {
                    //                Toast.makeText(SigninActivity.this, "error", Toast.LENGTH_SHORT).show();
                    //            }
                    //
                    //            @Override
                    //            public void onNext(AccessToken accessToken) {
                    //                PreferHelper.setWeiboAccessToken(accessToken);
                    //            }
                    //        });
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

        });
    }

}
