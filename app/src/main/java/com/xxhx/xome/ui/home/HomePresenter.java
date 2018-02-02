package com.xxhx.xome.ui.home;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.google.common.reflect.TypeToken;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Constants;
import com.xxhx.xome.helper.CacheHelper;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.helper.PreferHelper;
import com.xxhx.xome.http.weibo.WeiboService;
import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.entity.CursorResult;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.manager.WeiboManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by xxhx on 2016/9/28.
 */
public class HomePresenter implements HomeContract.Presenter {

    private static final String WeiboDiscCache = "weibo.lsp";

    @NonNull
    private HomeContract.View mHomeView;

    private WeiboManager mWeiboManager;

    public HomePresenter(@NonNull HomeContract.View homeView) {
        mHomeView = checkNotNull(homeView);
        mWeiboManager = WeiboManager.getInstance();
    }

    @Override
    public void loadStatuses() {
        AccessToken token = mWeiboManager.getAccessToken();
        mWeiboManager.loadHomeTimeline(new Subscriber<CursorResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mHomeView.toast(ContextHelper.getString(R.string.error_unknown), Toast.LENGTH_SHORT, true);
            }

            @Override
            public void onNext(CursorResult listCursorResult) {
                CacheHelper.getInstance().cacheInDisc(WeiboDiscCache, listCursorResult.getStatuses());
                mHomeView.refreshWeiboListView(listCursorResult.getStatuses());
            }
        }, token.getAccess_token());
    }

    @Override
    public void loadMoreStatuses(long maxId) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("max_id", Long.toString(maxId));
        AccessToken token = mWeiboManager.getAccessToken();
        mWeiboManager.loadHomeTimeline(new Subscriber<CursorResult>() {
            @Override
            public void onCompleted() {
                mHomeView.setLoadingMoreState(false);
            }

            @Override
            public void onError(Throwable e) {
                mHomeView.toast(ContextHelper.getString(R.string.error_unknown), Toast.LENGTH_SHORT, true);
                mHomeView.setLoadingMoreState(false);
            }

            @Override
            public void onNext(CursorResult listCursorResult) {
                if(listCursorResult.getStatuses().isEmpty()) {
                    mHomeView.toast(ContextHelper.getString(R.string.tip_no_more), Toast.LENGTH_SHORT, true);
                }
                else {
                    mHomeView.addMoreToWeiboListView(listCursorResult.getStatuses());
                }
            }
        }, token.getAccess_token(), options);
        mHomeView.setLoadingMoreState(true);
    }

    private void initStatuses() {
        List<Status> statuses = CacheHelper.getInstance().readFromDisc(WeiboDiscCache, new TypeToken<List<Status>>() {}.getType());
        if(statuses == null || statuses.size() == 0) {
            loadStatuses();
        }
        else {
            mHomeView.refreshWeiboListView(statuses);
        }
    }

    @Override
    public void subscribe() {
        AccessToken token = mWeiboManager.getAccessToken();
        if(token != null) {
            if(mHomeView.showWeiboListView()) {
                initStatuses();
            }
        }
        else {
            mHomeView.showSigninView();
        }
    }

    @Override
    public void unsubscribe() {

    }
}
