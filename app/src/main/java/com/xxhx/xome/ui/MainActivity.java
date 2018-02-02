package com.xxhx.xome.ui;

import android.content.Intent;
import android.os.Build;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.test.TestActivity;
import com.xxhx.xome.ui.disc.DiscFragment;
import com.xxhx.xome.ui.home.HomeFragment;
import com.xxhx.xome.ui.home.SendWeiboFragment;
import com.xxhx.xome.ui.mess.MessFragment;
import com.xxhx.xome.ui.prof.ProfFragment;
import com.xxhx.xome.view.BadgeView;

public class MainActivity extends BaseActivity implements View.OnClickListener, MainContract.View {

    private static final String TAG_SEND_WEIBO = "tag_send_weibo";

    private TextView mTitleView;
    private BadgeView mHomeBadge;
    private BadgeView mMessBadge;
    private BadgeView mDiscBadge;
    private BadgeView mProfBadge;
    private View mAddBtn;
    private View mHomeBtn;
    private View mMessBtn;
    private View mDiscBtn;
    private View mProfBtn;
    private View mHomeIndicator;
    private View mMessIndicator;
    private View mDiscIndicator;
    private View mProfIndicator;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private HomeFragment mHomeFragment;
    private MessFragment mMessFragment;
    private DiscFragment mDiscFragment;
    private ProfFragment mProfFragment;

    private int mSelection = -1;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mTitleView = (TextView) findViewById(R.id.title);
        mHomeBadge = (BadgeView) findViewById(R.id.badge_home);
        mMessBadge = (BadgeView) findViewById(R.id.badge_mess);
        mDiscBadge = (BadgeView) findViewById(R.id.badge_disc);
        mProfBadge = (BadgeView) findViewById(R.id.badge_prof);
        mAddBtn = findViewById(R.id.btn_add);
        mHomeBtn = findViewById(R.id.btn_home);
        mMessBtn = findViewById(R.id.btn_mess);
        mDiscBtn = findViewById(R.id.btn_disc);
        mProfBtn = findViewById(R.id.btn_prof);
        mHomeIndicator = findViewById(R.id.indicator_home);
        mMessIndicator = findViewById(R.id.indicator_mess);
        mDiscIndicator = findViewById(R.id.indicator_disc);
        mProfIndicator = findViewById(R.id.indicator_prof);
        mAddBtn.setOnClickListener(this);
        mHomeBtn.setOnClickListener(this);
        mMessBtn.setOnClickListener(this);
        mDiscBtn.setOnClickListener(this);
        mProfBtn.setOnClickListener(this);

        initViewPager();
        mViewPager.setCurrentItem(0);
        mHomeBtn.setSelected(true);
        mHomeIndicator.setVisibility(View.VISIBLE);
        setSelection(0);
    }

    private void initViewPager() {
        mHomeFragment = HomeFragment.newInstance(0);
        mMessFragment = MessFragment.newInstance(1);
        mDiscFragment = DiscFragment.newInstance(2);
        mProfFragment = ProfFragment.newInstance(3);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
                setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelection(int selection) {
        if(mSelection == selection) {
            return;
        }
        mSelection = selection;
        int themeColor = 0;
        int themeDarkColor = 0;
        switch (mSelection) {
            case 0:
                themeColor = mHomeFragment.getThemeColor();
                themeDarkColor = mHomeFragment.getThemeDarkColor();
                mTitleView.setText(mHomeFragment.getTitle());
                mHomeBtn.setSelected(true);
                mMessBtn.setSelected(false);
                mDiscBtn.setSelected(false);
                mProfBtn.setSelected(false);
                mHomeIndicator.setVisibility(View.VISIBLE);
                mMessIndicator.setVisibility(View.GONE);
                mDiscIndicator.setVisibility(View.GONE);
                mProfIndicator.setVisibility(View.GONE);
                break;
            case 1:
                themeColor = mMessFragment.getThemeColor();
                themeDarkColor = mMessFragment.getThemeDarkColor();
                mTitleView.setText(mMessFragment.getTitle());
                mHomeBtn.setSelected(false);
                mMessBtn.setSelected(true);
                mDiscBtn.setSelected(false);
                mProfBtn.setSelected(false);
                mHomeIndicator.setVisibility(View.GONE);
                mMessIndicator.setVisibility(View.VISIBLE);
                mDiscIndicator.setVisibility(View.GONE);
                mProfIndicator.setVisibility(View.GONE);
                break;
            case 2:
                themeColor = mDiscFragment.getThemeColor();
                themeDarkColor = mDiscFragment.getThemeDarkColor();
                mTitleView.setText(mDiscFragment.getTitle());
                mHomeBtn.setSelected(false);
                mMessBtn.setSelected(false);
                mDiscBtn.setSelected(true);
                mProfBtn.setSelected(false);
                mHomeIndicator.setVisibility(View.GONE);
                mMessIndicator.setVisibility(View.GONE);
                mDiscIndicator.setVisibility(View.VISIBLE);
                mProfIndicator.setVisibility(View.GONE);
                break;
            case 3:
                themeColor = mProfFragment.getThemeColor();
                themeDarkColor = mProfFragment.getThemeDarkColor();
                mTitleView.setText(mProfFragment.getTitle());
                mHomeBtn.setSelected(false);
                mMessBtn.setSelected(false);
                mDiscBtn.setSelected(false);
                mProfBtn.setSelected(true);
                mHomeIndicator.setVisibility(View.GONE);
                mMessIndicator.setVisibility(View.GONE);
                mDiscIndicator.setVisibility(View.GONE);
                mProfIndicator.setVisibility(View.VISIBLE);
                break;
        }
        mTitleView.setBackgroundColor(themeColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(themeDarkColor);
        }
    }

    @Override
    public void showHomeBadge(int count) {
        mHomeBadge.showBadgeCount(count);
    }

    @Override
    public void showMessBadge(int count) {
        mMessBadge.showBadgeCount(count);
    }

    @Override
    public void showDiscBadge(int count) {
        mDiscBadge.showBadgeCount(count);
    }

    @Override
    public void showProfBadge(int count) {
        mProfBadge.showBadgeCount(count);
    }

    private void showSendWeiboFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new SendWeiboFragment();
        transaction.add(R.id.top_content, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                showSendWeiboFragment();
                break;
            case R.id.btn_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.btn_mess:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.btn_disc:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.btn_prof:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    public void test(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            switch (position) {
                case 0:
                    return mHomeFragment;
                case 1:
                    return mMessFragment;
                case 2:
                    return mDiscFragment;
                case 3:
                    return mProfFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).getTitle();
        }
    }
}
