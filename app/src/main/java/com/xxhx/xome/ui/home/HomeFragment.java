package com.xxhx.xome.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.ui.BaseFragment;
import com.xxhx.xome.ui.home.signin.SigninActivity;
import com.xxhx.xome.view.DividerItemDecoration;
import java.util.List;

/**
 * Created by xxhx on 2016/9/19.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    private HomeContract.Presenter mPresenter;

    private View mBtnSignin;
    private StatusMenuWindow mStatusMenuWindow;
    private SwipeRefreshLayout mWeiboRefreshView;
    private View mBtnTop;
    private RecyclerView mWeiboListView;
    private WeiboStatusAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String retrieveTitle() {
        return ContextHelper.getString(R.string.home);
    }

    @Override
    public int retrieveThemeColor() {
        return ContextHelper.getColor(R.color.home);
    }

    @Override
    public int retrieveThemeDarkColor() {
        return ContextHelper.getColor(R.color.darkHome);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mBtnSignin = rootView.findViewById(R.id.signin);
        mBtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);
            }
        });
        mWeiboRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.weibo_refresh);
        mWeiboRefreshView.setColorSchemeResources(R.color.home, R.color.mess, R.color.disc, R.color.prof);
        mWeiboRefreshView.setOnRefreshListener(this);
        mBtnTop = rootView.findViewById(R.id.top);
        mBtnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboListView.smoothScrollToPosition(0);
            }
        });
        mWeiboListView = (RecyclerView) rootView.findViewById(R.id.weibo_list);
        mLayoutManager = new LinearLayoutManager(getContext());
        mWeiboListView.setLayoutManager(mLayoutManager);
        mAdapter = new WeiboStatusAdapter(this, mPresenter, null);
        mWeiboListView.setAdapter(mAdapter);
        mWeiboListView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mWeiboListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mStatusMenuWindow.isShowing()) {
                    mStatusMenuWindow.getContentView().setVisibility(View.GONE);
                    mStatusMenuWindow.dismiss();
                }
                return false;
            }
        });
        mWeiboListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(mLayoutManager.findFirstVisibleItemPosition() > 0) {
                    showTopBtn();
                }
                else {
                    hideTopBtn();
                }
            }
        });
        initPopupWindow();
        //mPresenter.subscribe();
        return rootView;
    }

    private void initPopupWindow() {
        //View popupView = View.inflate(getContext(), R.layout.layout_weibo_options, null);
        mStatusMenuWindow = new StatusMenuWindow(getContext());
        //mPopupWindow.setAnimationStyle(R.style.PopupMenuStyle);
        mAdapter.setStatusMenuListnener(new WeiboStatusAdapter.StatusMenuListnener() {
            @Override
            public void onStatusMenuShown(View view, Status status) {
                if(mStatusMenuWindow.isShowing()) {
                    mStatusMenuWindow.dismiss();
                }
                mStatusMenuWindow.showAsDropDown(status, view);
            }

        });
    }

    private void showTopBtn() {
        if(mBtnTop.getVisibility() != View.VISIBLE) {
            mBtnTop.setVisibility(View.VISIBLE);
            Animation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(200);
            mBtnTop.startAnimation(scaleAnimation);
        }
    }

    private void hideTopBtn() {
        if(mBtnTop.getVisibility() != View.GONE) {
            mBtnTop.setVisibility(View.GONE);
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(200);
            mBtnTop.startAnimation(scaleAnimation);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public boolean showSigninView() {
        if(mBtnSignin.getVisibility() != View.VISIBLE) {
            mWeiboRefreshView.setVisibility(View.GONE);
            mBtnSignin.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean showWeiboListView() {
        if(mWeiboRefreshView.getVisibility() != View.VISIBLE) {
            mBtnSignin.setVisibility(View.GONE);
            mWeiboRefreshView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public void refreshWeiboListView(List<Status> statuses) {
        mAdapter.setStatuses(statuses);
        if(mWeiboRefreshView.isRefreshing()) {
            mWeiboRefreshView.setRefreshing(false);
        }
    }

    @Override
    public void addMoreToWeiboListView(List<Status> statuses) {
        mAdapter.addStatuses(statuses);
    }

    @Override
    public void setLoadingMoreState(boolean loadingMore) {
        mAdapter.setLoadingMoreState(loadingMore);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void toast(CharSequence text, int duration, boolean cancelRefresh) {
        Toast.makeText(getContext(), text, duration).show();
        if(cancelRefresh && mWeiboRefreshView.isRefreshing()) {
            mWeiboRefreshView.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadStatuses();
    }
}
