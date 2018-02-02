package com.xxhx.xome.ui.prof;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.http.weibo.entity.User;
import com.xxhx.xome.ui.BaseFragment;
import com.xxhx.xome.ui.prof.function.OfflineMapActivity;

/**
 * Created by xxhx on 2016/9/19.
 */
public class ProfFragment extends BaseFragment implements ProfContract.View, View.OnClickListener {

    private ProfContract.Presenter mPresenter;

    private ImageView mAvatarView;
    private TextView mNameView;
    private TextView mDescriptionView;
    private TextView mStatusesCountView;
    private TextView mFriendsCountView;
    private TextView mFollowersCountView;

    private View mBtnFunction;

    public static ProfFragment newInstance(int sectionNumber) {
        ProfFragment fragment = new ProfFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String retrieveTitle() {
        return ContextHelper.getString(R.string.prof);
    }

    @Override
    public int retrieveThemeColor() {
        return ContextHelper.getColor(R.color.prof);
    }

    @Override
    public int retrieveThemeDarkColor() {
        return ContextHelper.getColor(R.color.darkProf);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProfPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prof, container, false);
        mAvatarView = (ImageView) rootView.findViewById(R.id.avatar);
        mNameView = (TextView) rootView.findViewById(R.id.name);
        mDescriptionView = (TextView) rootView.findViewById(R.id.description);
        mStatusesCountView = (TextView) rootView.findViewById(R.id.statuses_count);
        mFriendsCountView = (TextView) rootView.findViewById(R.id.friends_count);
        mFollowersCountView = (TextView) rootView.findViewById(R.id.followers_count);
        mBtnFunction = rootView.findViewById(R.id.function);
        initView();
        mPresenter.subscribe();
        return rootView;
    }

    private void initView() {
        mBtnFunction.setOnClickListener(this);
    }

    @Override
    public void updateInfoView(User user) {
        Glide.with(this).load(user.getAvatarLarge()).error(R.mipmap.ic_launcher2).into(mAvatarView);
        mNameView.setText(user.getName());
        mDescriptionView.setText(user.getDescription());
        mStatusesCountView.setText(String.valueOf(user.getStatusesCount()));
        mFriendsCountView.setText(String.valueOf(user.getFriendsCount()));
        mFollowersCountView.setText(String.valueOf(user.getFollowersCount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.function:
                gotoFunction();
                break;
        }
    }

    private void gotoFunction() {
        Intent intent = new Intent(getActivity(), OfflineMapActivity.class);
        startActivity(intent);
    }
}
