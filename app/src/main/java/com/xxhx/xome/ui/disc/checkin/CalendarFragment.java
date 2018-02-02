package com.xxhx.xome.ui.disc.checkin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.ui.disc.checkin.data.Checkin;
import com.xxhx.xome.ui.disc.checkin.view.CalendarView;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xxhx on 2017/8/3.
 */

public class CalendarFragment extends Fragment {
    public static final String ARG_DAY = "arg_day";
    public static final String ARG_MONTH = "arg_month";
    public static final String ARG_YEAR = "arg_year";

    private int day;
    private int month;
    private int year;

    private CalendarView mCalendarView;

    private CheckinActivity mActivity;

    public static CalendarFragment newInstance(int day, int month, int year) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_DAY, day);
        bundle.putInt(ARG_MONTH, month);
        bundle.putInt(ARG_YEAR, year);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        day = bundle.getInt(ARG_DAY);
        month = bundle.getInt(ARG_MONTH);
        year = bundle.getInt(ARG_YEAR);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCalendarView != null && mActivity != null) {
            int selectedDate = mActivity.getSelectedDate();
            if((year * 100 + month) == (selectedDate / 100)) {
                mCalendarView.setSelectedDay(selectedDate % 100);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mActivity = (CheckinActivity) getActivity();
        mCalendarView = new CalendarView(mActivity);
        mCalendarView.setId(year * 100 + month);
        mCalendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int position, Calendar date, List<Checkin> checkins) {
                mActivity.performDateSelected(date, checkins);
            }

        });
        mCalendarView.init(year, month, day);
        return mCalendarView;
    }

}
