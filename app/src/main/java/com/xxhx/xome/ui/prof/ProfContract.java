package com.xxhx.xome.ui.prof;

import com.xxhx.xome.http.weibo.entity.User;
import com.xxhx.xome.ui.BasePresenter;

/**
 * Created by xxhx on 2016/10/9.
 */
public interface ProfContract {
    interface View {
        void updateInfoView(User user);
    }

    interface Presenter extends BasePresenter {

    }
}
