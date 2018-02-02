package com.xxhx.xome.ui.home;

import android.support.annotation.StringRes;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.ui.BasePresenter;
import java.util.List;

/**
 * Created by xxhx on 2016/9/28.
 */
public interface HomeContract {
    interface View {
        boolean showSigninView();
        boolean showWeiboListView();
        void refreshWeiboListView(List<Status> statuses);
        void addMoreToWeiboListView(List<Status> statuses);
        void setLoadingMoreState(boolean loadingMore);
        void toast(CharSequence text, int duration, boolean cancelRefresh);
    }

    interface Presenter extends BasePresenter {
        void loadStatuses();
        void loadMoreStatuses(long maxId);
    }
}
