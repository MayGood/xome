package com.xxhx.xome.ui.disc;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;
import com.xxhx.xome.ui.BasePresenter;

/**
 * Created by xxhx on 2016/9/21.
 */
public interface DiscContract {
    interface View {
        /**
         * Add public item
         * @param itemId
         * @param iconResId
         * @param title
         * @param message
         * @param badgeCount
         * @param onClickListener
         * @return the added View's id
         */
        int addPublicItem(int itemId, @DrawableRes int iconResId, String title, String message, int badgeCount,
                android.view.View.OnClickListener onClickListener);

        /**
         * Add private item
         * @param itemId
         * @param iconResId
         * @param title
         * @param message
         * @param badgeCount
         * @param onClickListener
         * @return the added View's id
         */
        int addPrivateItem(int itemId, @DrawableRes int iconResId, String title, String message, int badgeCount,
                android.view.View.OnClickListener onClickListener);
        void updatePublicItem(int viewId, String message, int badgeCount);
        void updatePrivateItem(int viewId, String message, int badgeCount);
    }

    interface Presenter extends BasePresenter {

    }
}
