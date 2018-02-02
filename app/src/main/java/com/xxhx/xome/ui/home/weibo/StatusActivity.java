package com.xxhx.xome.ui.home.weibo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.entity.Comment;
import com.xxhx.xome.http.weibo.entity.CursorResult;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.manager.WeiboManager;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.home.adapter.CommentAdapter;
import com.xxhx.xome.ui.home.adapter.RepostAdapter;
import com.xxhx.xome.ui.home.view.StatusView;
import java.util.List;
import rx.Subscriber;

public class StatusActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_STATUS = "extra_status";

    private Status mStatus;

    private RepostAdapter mRepostAdapter;
    private CommentAdapter mCommentAdapter;

    private View mHeaderView;
    private StatusView mStatusView;
    private ListView mListView;
    private ViewHolder mViewHolder;

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String data = getIntent().getStringExtra(EXTRA_STATUS);
        if(!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            mStatus = gson.fromJson(data, Status.class);
        }

        initView();
    }

    private void initView() {
        mHeaderView = findViewById(R.id.header);
        mListView = (ListView) findViewById(R.id.list);
        mRepostAdapter = new RepostAdapter(this);
        mCommentAdapter = new CommentAdapter(this);
        mListView.setAdapter(mCommentAdapter);
        View view = LayoutInflater.from(this).inflate(R.layout.list_item_status_header, null);
        mStatusView = (StatusView) view.findViewById(R.id.status);
        mStatusView.setStatus(mStatus);
        View listHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_status_header, null);
        mListView.addHeaderView(view);
        mListView.addHeaderView(listHeaderView);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if(firstVisibleItem > 0) {
                    mHeaderView.setVisibility(View.VISIBLE);
                }
                else {
                    mHeaderView.setVisibility(View.GONE);
                }
            }
        });
        mViewHolder = new ViewHolder();
        mViewHolder.repost = ((TextView) mHeaderView.findViewById(R.id.repost));
        mViewHolder.comment = ((TextView) mHeaderView.findViewById(R.id.comment));
        mViewHolder.like = ((TextView) mHeaderView.findViewById(R.id.like));
        mViewHolder.toggleBar = mHeaderView.findViewById(R.id.toggle_bar);
        mViewHolder.listRepost = ((TextView) listHeaderView.findViewById(R.id.repost));
        mViewHolder.listComment = ((TextView) listHeaderView.findViewById(R.id.comment));
        mViewHolder.listLike = ((TextView) listHeaderView.findViewById(R.id.like));
        mViewHolder.listToggleBar = listHeaderView.findViewById(R.id.toggle_bar);
        mViewHolder.repost.setOnClickListener(this);
        mViewHolder.listRepost.setOnClickListener(this);
        mViewHolder.comment.setOnClickListener(this);
        mViewHolder.listComment.setOnClickListener(this);
        refreshHeaderCount(mStatus.getRepostsCount(), mStatus.getCommentsCount(), mStatus.getAttitudesCount());
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectDataType(1);
            }
        }, 1000);
    }

    /**
     * 选择展现哪类数据
     * @param type 0-repost, 1-comment, 2-like
     */
    private void selectDataType(int type) {
        boolean repost = false;
        boolean comment = false;
        boolean like = false;
        float translationX = 0;

        WeiboManager weiboManager = WeiboManager.getInstance();
        AccessToken token = weiboManager.getAccessToken();
        if(type == 0) {
            repost = true;
            translationX = mViewHolder.listRepost.getLeft() / 2 + mViewHolder.listRepost.getRight() / 2 - mViewHolder.listToggleBar.getWidth() / 2;
            mListView.setAdapter(mRepostAdapter);
            WeiboManager.getInstance().loadStatusReposts(new Subscriber<CursorResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastHelper.toast("failed");
                }

                @Override
                public void onNext(CursorResult cursorResult) {
                    List<Status> reposts = cursorResult.getReposts();
                    if(reposts == null || reposts.size() == 0) {
                        ToastHelper.toast("0 reposts");
                    }
                    else {
                        mRepostAdapter.clear();
                        mRepostAdapter.addAll(reposts);
                        mRepostAdapter.notifyDataSetChanged();
                    }
                }
            }, token.getAccess_token(), mStatus.getId());
        }
        else if(type == 1) {
            comment = true;
            translationX = mViewHolder.listComment.getLeft() / 2 + mViewHolder.listComment.getRight() / 2 - mViewHolder.listToggleBar.getWidth() / 2;
            mListView.setAdapter(mCommentAdapter);
            WeiboManager.getInstance().loadStatusComments(new Subscriber<CursorResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastHelper.toast("failed");
                }

                @Override
                public void onNext(CursorResult cursorResult) {
                    List<Comment> comments = cursorResult.getComments();
                    if(comments == null || comments.size() == 0) {
                        ToastHelper.toast("0 comments");
                    }
                    else {
                        mCommentAdapter.clear();
                        mCommentAdapter.addAll(comments);
                        mCommentAdapter.notifyDataSetChanged();
                    }
                }
            }, token.getAccess_token(), mStatus.getId());
        }
        else if(type == 2) {
            like = true;
            translationX = mViewHolder.listLike.getLeft() / 2 + mViewHolder.listLike.getRight() / 2 - mViewHolder.listToggleBar.getWidth() / 2;
        }
        mViewHolder.repost.setSelected(repost);
        mViewHolder.listRepost.setSelected(repost);
        mViewHolder.comment.setSelected(comment);
        mViewHolder.listComment.setSelected(comment);
        mViewHolder.like.setSelected(like);
        mViewHolder.listLike.setSelected(like);
        if(mHeaderView.getVisibility() == View.VISIBLE) {
            mViewHolder.toggleBar.setVisibility(View.VISIBLE);
            mViewHolder.toggleBar.animate().translationX(translationX).setDuration(200).start();
        }
        else {
            mViewHolder.listToggleBar.setVisibility(View.VISIBLE);
            mViewHolder.listToggleBar.animate().translationX(translationX).setDuration(200).start();
        }
    }

    private void refreshHeaderCount(int repost, int comment, int like) {
        if(repost > 0) {
            String text = getString(R.string.repost) + " " + repost;
            mViewHolder.repost.setText(text);
            mViewHolder.listRepost.setText(text);
        }
        else {
            mViewHolder.repost.setText(R.string.repost);
            mViewHolder.listRepost.setText(R.string.repost);
        }

        if(comment > 0) {
            String text = getString(R.string.comment) + " " + comment;
            mViewHolder.comment.setText(text);
            mViewHolder.listComment.setText(text);
        }
        else {
            mViewHolder.comment.setText(R.string.comment);
            mViewHolder.listComment.setText(R.string.comment);
        }

        if(like > 0) {
            String text = getString(R.string.like) + " " + like;
            mViewHolder.like.setText(text);
            mViewHolder.listLike.setText(text);
        }
        else {
            mViewHolder.like.setText(R.string.like);
            mViewHolder.listLike.setText(R.string.like);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repost:
                selectDataType(0);
                break;
            case R.id.comment:
                selectDataType(1);
                break;
        }
    }

    class ViewHolder {
        TextView repost;
        TextView comment;
        TextView like;
        View toggleBar;
        TextView listRepost;
        TextView listComment;
        TextView listLike;
        View listToggleBar;
    }

}
