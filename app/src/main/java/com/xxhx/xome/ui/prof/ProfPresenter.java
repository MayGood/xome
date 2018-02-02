package com.xxhx.xome.ui.prof;

import android.support.annotation.NonNull;
import android.widget.Toast;
import com.xxhx.xome.helper.CacheHelper;
import com.xxhx.xome.helper.PreferHelper;
import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.entity.User;
import com.xxhx.xome.manager.WeiboManager;
import rx.Subscriber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by xxhx on 2016/10/9.
 */
public class ProfPresenter implements ProfContract.Presenter {
    private static final String InfoDiscCache = "prof_info_%s.sp";

    @NonNull
    private ProfContract.View mProfView;

    public ProfPresenter(@NonNull ProfContract.View profView) {
        mProfView = checkNotNull(profView);
    }

    private void loadInfo(@NonNull AccessToken token) {
        User info = CacheHelper.getInstance().readFromDisc(String.format(InfoDiscCache, token.getUid()), User.class);
        if(info != null) {
            mProfView.updateInfoView(info);
        }
        else {
            WeiboManager.getInstance().loadUserInfo(new Subscriber<User>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.toString();
                }

                @Override
                public void onNext(User user) {
                    CacheHelper.getInstance().cacheInDisc(String.format(InfoDiscCache, user.getIdstr()), user);
                    mProfView.updateInfoView(user);
                }
            }, token.getAccess_token(), Long.parseLong(token.getUid()));
        }
    }

    @Override
    public void subscribe() {
        AccessToken token = WeiboManager.getInstance().getAccessToken();
        if(token != null) {
            loadInfo(token);
        }
    }

    @Override
    public void unsubscribe() {

    }
}
