package com.xxhx.xome.ui;

/**
 * Created by xxhx on 2016/9/19.
 */
public interface MainContract {
    interface View {
        void showHomeBadge(int count);
        void showMessBadge(int count);
        void showDiscBadge(int count);
        void showProfBadge(int count);
    }

    interface Presenter extends BasePresenter {

    }
}
