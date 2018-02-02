package com.xxhx.xome.ui.home;

import android.content.Context;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.http.weibo.entity.Status;

/**
 * Created by xxhx on 2017/2/25.
 */

public class StatusMenuWindow extends PopupWindow {
    private View mBtnComment;
    private View mBtnRepost;
    private View mBtnCollect;

    private Status mStatus;

    public StatusMenuWindow(Context context) {
        super(720, 120);
        View popupView = View.inflate(context, R.layout.layout_weibo_options, null);
        mBtnComment = popupView.findViewById(R.id.comment);
        mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.toast(ContextHelper.getString(R.string.developing));
                dismiss();
            }
        });
        mBtnRepost = popupView.findViewById(R.id.repost);
        mBtnRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.toast(ContextHelper.getString(R.string.developing));
                dismiss();
            }
        });
        mBtnCollect = popupView.findViewById(R.id.collect);
        mBtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.toast(ContextHelper.getString(R.string.developing));
                dismiss();
            }
        });
        setContentView(popupView);
    }

    public void showAsDropDown(Status status, View view) {
        mStatus = status;
        getContentView().setVisibility(View.VISIBLE);
        PopupWindowCompat.showAsDropDown(this, view, -96, -96, Gravity.RIGHT|Gravity.BOTTOM);
    }
}
