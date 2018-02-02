package com.xxhx.xome.ui.disc.checkin.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xxhx.xome.R;
import java.util.Calendar;

/**
 * Created by xxhx on 2017/8/3.
 */

public class SuperCalendarView extends LinearLayout {
    private TextView mLabelMonthName;
    private TextView mLabelCheckinInfo;
    private TextView mLabelCheckoutInfo;

    private ViewPager pager;

    public SuperCalendarView(Context context) {
        super(context);
    }

    public SuperCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_calendar, this);
        mLabelMonthName = (TextView) findViewById(R.id.text_month_name);
        mLabelCheckinInfo = (TextView) findViewById(R.id.text_checkin_info);
        mLabelCheckoutInfo = (TextView) findViewById(R.id.text_checkout_info);
        pager = (ViewPager) findViewById(R.id.pager);
        //CalendarPagerAdapter adapter = new CalendarPagerAdapter(getChildFragmentManager(), pager);
    }

    class CalendarPagerAdapter extends FragmentPagerAdapter implements
            ViewPager.OnPageChangeListener {

        public CalendarPagerAdapter(FragmentManager fm, ViewPager viewPager) {
            super(fm);
            viewPager.setAdapter(this);
            viewPager.addOnPageChangeListener(this);
        }

        @Override
        public Fragment getItem(int position) {
            //Calendar date = Calendar.getInstance();
            //date.set(1970, Calendar.JANUARY, 1);
            //date.add(Calendar.MONTH, position);
            //return MonthFragment.newInstance(date.get(Calendar.DAY_OF_MONTH),
            //        date.get(Calendar.MONTH), date.get(Calendar.YEAR));
            return null;
        }

        @Override
        public int getCount() {
            // from 1970-1-1 to 2037-12-31
            return 816;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(0);
            date.add(Calendar.MONTH, position);
            mLabelMonthName.setText(
                    date.get(Calendar.YEAR) + "年" + (date.get(Calendar.MONTH) + 1) + "月");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
