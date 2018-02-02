package com.xxhx.xome.ui.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.StatusHelper;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.http.weibo.entity.User;
import com.xxhx.xome.manager.WeiboManager;
import com.xxhx.xome.ui.activity.WebViewActivity;
import com.xxhx.xome.ui.home.viewholder.ContentViewHolder;
import com.xxhx.xome.ui.home.viewholder.FooterViewHolder;
import com.xxhx.xome.ui.home.viewholder.ViewHolder;
import com.xxhx.xome.ui.home.weibo.StatusActivity;
import com.xxhx.xome.view.WeiboPicsView;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * Created by xxhx on 2016/9/29.
 */
public class WeiboStatusAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final Fragment mFragment;
    private final HomeContract.Presenter mPresenter;
    private List<Status> mStatuses;

    private boolean loadingMore;

    public interface StatusMenuListnener {
        void onStatusMenuShown(View view, Status status);
    }

    private StatusMenuListnener mStatusMenuListener;

    public WeiboStatusAdapter(@NonNull Fragment fragment, HomeContract.Presenter presenter, List<Status> statuses) {
        mFragment = fragment;
        mPresenter = presenter;
        mStatuses = new ArrayList<Status>();
        if(statuses != null && statuses.size() > 0) {
            mStatuses.addAll(statuses);
        }
    }

    public void setLoadingMoreState(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }

    public void setStatusMenuListnener(StatusMenuListnener listener) {
        mStatusMenuListener = listener;
    }

    public void setStatuses(List<Status> statuses) {
        mStatuses.clear();
        if(statuses != null && statuses.size() > 0) {
            mStatuses.addAll(statuses);
        }
        notifyDataSetChanged();
    }

    public void addStatuses(List<Status> statuses) {
        if(statuses != null && statuses.size() > 0) {
            mStatuses.addAll(statuses);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ViewHolder.VIEW_TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_home_footer, parent, false);
            final FooterViewHolder vh = new FooterViewHolder(v);
            vh.mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPresenter != null) {
                        long maxId = 0;
                        if(mStatuses.size() > 0) {
                            maxId = mStatuses.get(mStatuses.size()-1).getId() - 1;
                        }
                        mPresenter.loadMoreStatuses(maxId);
                    }
                }
            });
            return vh;
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_home, parent, false);
        ContentViewHolder vh = new ContentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        int viewType = vh.getViewType();
        if(viewType == ViewHolder.VIEW_TYPE_CONTENT) {
            ContentViewHolder holder = (ContentViewHolder) vh;
            final Status status = mStatuses.get(position);
            holder.statusView.setStatus(status);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mFragment.getContext(), StatusActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra(StatusActivity.EXTRA_STATUS, gson.toJson(status));
                    mFragment.startActivity(intent);
                }
            });
        }
        else if(viewType == ViewHolder.VIEW_TYPE_FOOTER) {
            final FooterViewHolder holder = (FooterViewHolder) vh;
            if(loadingMore) {
                holder.mLabel.setText(R.string.loading);
                holder.mProgressBar.setVisibility(View.VISIBLE);
                holder.mRootView.setClickable(false);
            }
            else {
                holder.mLabel.setText(R.string.load_more);
                holder.mProgressBar.setVisibility(View.GONE);
                holder.mRootView.setClickable(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mStatuses.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position >= mStatuses.size()) {
            return ViewHolder.VIEW_TYPE_FOOTER;
        }
        return ViewHolder.VIEW_TYPE_CONTENT;
    }
}
