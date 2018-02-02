package com.xxhx.xome.ui.home.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xxhx on 2017/2/17.
 */

public abstract class ViewHolder extends RecyclerView.ViewHolder {

    public static final int VIEW_TYPE_CONTENT = 2;
    public static final int VIEW_TYPE_FOOTER = 3;

    public View mRootView;

    public ViewHolder(View itemView) {
        super(itemView);
        mRootView = itemView;
    }

    public abstract int getViewType();
}
