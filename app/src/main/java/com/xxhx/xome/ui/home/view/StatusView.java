package com.xxhx.xome.ui.home.view;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.StatusHelper;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.http.weibo.entity.User;
import com.xxhx.xome.view.WeiboPicsView;

/**
 * Created by xxhx on 2017/7/19.
 */

public class StatusView extends FrameLayout {

    private ImageView mAvatar;
    private TextView mScreenName;
    private TextView mText;
    private WeiboPicsView mPicsView;
    private TextView mRelativeTime;
    private View mOptions;
    private ViewGroup mRetweeted;
    private TextView mRetweetedScreenName;
    private TextView mRetweetedText;
    private WeiboPicsView mRetweetedPicsView;

    public StatusView(Context context) {
        super(context);
        init(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_status_view, this);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mScreenName = (TextView) findViewById(R.id.screen_name);
        mText = (TextView) findViewById(R.id.text);
        mPicsView = (WeiboPicsView) findViewById(R.id.pics);
        mRelativeTime = (TextView) findViewById(R.id.relative_time);
        mOptions = findViewById(R.id.options);
        mRetweeted = (ViewGroup) findViewById(R.id.retweeted);
        mRetweetedScreenName = (TextView) findViewById(R.id.retweeted_screen_name);
        mRetweetedText = (TextView) findViewById(R.id.retweeted_text);
        mRetweetedPicsView = (WeiboPicsView) findViewById(R.id.retweeted_pics);
        mText.setMovementMethod(LinkMovementMethod.getInstance());
        mRetweetedText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setStatus(Status status) {
        User user = status.getUser();
        Glide.with(getContext()).load(user.getAvatarLarge()).into(mAvatar);
        mScreenName.setText(user.getScreenName());
        mText.setText(StatusHelper.getDisplayStatusText(getContext(), status.getText()));
        mPicsView.setStatus(status);
        mRelativeTime.setText(
                DateUtils.getRelativeDateTimeString(getContext(), status.getCreatedAt().getTime(),
                        DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME));
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(mStatusMenuListener != null) {
                //    mStatusMenuListener.onStatusMenuShown(v, status);
                //}
            }
        });
        Status retweeted = status.getRetweetedStatus();
        if(retweeted != null) {
            if(retweeted.getUser() == null) {
                mRetweetedScreenName.setVisibility(View.GONE);
            }
            else {
                mRetweetedScreenName.setText(retweeted.getUser().getScreenName());
                mRetweetedScreenName.setVisibility(View.VISIBLE);
            }
            mRetweetedText.setText(StatusHelper.getDisplayStatusText(getContext(), retweeted.getText()));
            mRetweetedPicsView.setStatus(retweeted);
            //if(retweeted.getPicCount() > 0) {
            //    holder.mRetweetedPicsView.setPics(mFragment, retweeted.getFirstPicUrl(), retweeted.getSquarePicUrls());
            //    holder.mRetweetedPicsView.setVisibility(View.VISIBLE);
            //}
            //else {
            //    holder.mRetweetedPicsView.setVisibility(View.GONE);
            //}
            mRetweeted.setVisibility(View.VISIBLE);
        }
        else {
            mRetweeted.setVisibility(View.GONE);
        }
        //holder.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent = new Intent(mFragment.getContext(), StatusActivity.class);
        //        Gson gson = new Gson();
        //        intent.putExtra(StatusActivity.EXTRA_STATUS, gson.toJson(status));
        //        mFragment.startActivity(intent);
        //    }
        //});
    }
}
