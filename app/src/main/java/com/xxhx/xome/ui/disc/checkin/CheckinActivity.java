package com.xxhx.xome.ui.disc.checkin;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.checkin.data.Checkin;
import com.xxhx.xome.ui.disc.checkin.util.WorkdayUtil;
import com.xxhx.xome.ui.disc.checkin.view.CalendarView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CheckinActivity extends BaseActivity {

    private SimpleDateFormat mFormatTime = new SimpleDateFormat("HH:mm:ss");

    private TextView mLabelMonthName;
    private TextView mLabelCheckinInfo;
    private TextView mLabelCheckoutInfo;
    private TextView mLabelRemark;
    private View mBtnOvertime;

    private ViewPager pager;

    private int mMonthOffset;
    private int mSelectedDay;
    private Checkin mSelectedCheckin;
    private Checkin mSelectedCheckout;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_checkin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLabelMonthName = (TextView) findViewById(R.id.text_month_name);
        mLabelCheckinInfo = (TextView) findViewById(R.id.text_checkin_info);
        mLabelCheckoutInfo = (TextView) findViewById(R.id.text_checkout_info);
        mLabelRemark = (TextView) findViewById(R.id.remark);
        mBtnOvertime = findViewById(R.id.btn_overtime);
        pager = (ViewPager) findViewById(R.id.pager);
        new CalendarPagerAdapter(getSupportFragmentManager(), pager);
    }

    private void showDate(int dayOfMonth, int month, int year) {
        int item = (year - 1970) * 12 + month;
        mMonthOffset = item;
        mSelectedDay = dayOfMonth;
        mLabelMonthName.setText(year + "年" + (month + 1) + "月");
        pager.setCurrentItem(item);
    }

    public int getSelectedDate() {
        int year = mMonthOffset / 12 + 1970;
        int month = mMonthOffset % 12;
        return year * 10000 + month * 100 + mSelectedDay;
    }

    public void navigation(int offset) {
        int item = pager.getCurrentItem() + offset;
        pager.setCurrentItem(item);
    }

    public void performDateSelected(Calendar date, List<Checkin> checkins) {
        mSelectedDay = date.get(Calendar.DAY_OF_MONTH);
        mSelectedCheckin = null;
        mSelectedCheckout = null;
        if (checkins != null) {
            for (Checkin item : checkins) {
                if (item.getCheckout()) {
                    if (mSelectedCheckout == null || mSelectedCheckout.getTime()
                            .before(item.getTime())) {
                        mSelectedCheckout = item;
                    }
                } else {
                    if (mSelectedCheckin == null || mSelectedCheckin.getTime()
                            .after(item.getTime())) {
                        mSelectedCheckin = item;
                    }
                }
            }
        }
        updateTextLabel();
    }

    private void updateTextLabel() {
        boolean showOvertimeBtn = true;
        Calendar date = Calendar.getInstance();
        int year = mMonthOffset / 12 + 1970;
        int month = mMonthOffset % 12;
        date.set(year, month, mSelectedDay);
        if(WorkdayUtil.isWorkday(date.getTimeInMillis())) {
            showOvertimeBtn = false;
        }
        if (mSelectedCheckin != null) {
            mLabelCheckinInfo.setText(getString(R.string.checkin_notice,
                    mFormatTime.format(mSelectedCheckin.getTime())));
            mLabelCheckinInfo.setVisibility(View.VISIBLE);
            showOvertimeBtn = false;
        } else {
            mLabelCheckinInfo.setVisibility(View.GONE);
        }
        if (mSelectedCheckout != null) {
            mLabelCheckoutInfo.setText(getString(R.string.checkout_notice,
                    mFormatTime.format(mSelectedCheckout.getTime())));
            mLabelCheckoutInfo.setVisibility(View.VISIBLE);
            showOvertimeBtn = false;
        } else {
            mLabelCheckoutInfo.setVisibility(View.GONE);
        }
        if(showOvertimeBtn) {
            mBtnOvertime.setVisibility(View.VISIBLE);
        }
        else {
            mBtnOvertime.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar now = Calendar.getInstance();
        showDate(now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH), now.get(Calendar.YEAR));
    }

    public void addCheckin(View view) {
        //View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_add_checkin, null);
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar date = Calendar.getInstance();
                int year = mMonthOffset / 12 + 1970;
                int month = mMonthOffset % 12;
                date.set(year, month, mSelectedDay, hourOfDay, minute);
                if(mSelectedCheckin == null) {
                    mSelectedCheckin = new Checkin();
                    mSelectedCheckin.setDate(date.get(Calendar.YEAR) * 10000 + date.get(Calendar.MONTH) * 100 + mSelectedDay);
                    mSelectedCheckin.setTime(date.getTime());
                    mSelectedCheckin.setCheckout(false);
                    App.getInstance().getDaoSession().getCheckinDao().insert(mSelectedCheckin);
                    CalendarView calendarView = getCalendarView(date);
                    calendarView.addCheckin(mSelectedCheckin);
                }
                else if(mSelectedCheckout == null) {
                    mSelectedCheckout = new Checkin();
                    mSelectedCheckout.setDate(date.get(Calendar.YEAR) * 10000 + date.get(Calendar.MONTH) * 100 + mSelectedDay);
                    mSelectedCheckout.setTime(date.getTime());
                    mSelectedCheckout.setCheckout(true);
                    App.getInstance().getDaoSession().getCheckinDao().insert(mSelectedCheckout);
                    CalendarView calendarView = getCalendarView(date);
                    calendarView.addCheckin(mSelectedCheckout);
                }
                else {
                    mSelectedCheckout.setTime(date.getTime());
                    App.getInstance().getDaoSession().getCheckinDao().update(mSelectedCheckout);
                }
                updateTextLabel();
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        //AlertDialog dialog = new AlertDialog.Builder(this).setTitle("手动打卡")
        //        .setMessage("8月7日\n\n打卡时间08:01")
        //        .setPositiveButton("确定", null)
        //        .setNegativeButton("取消", null)
        //        .create();
        //dialog.show();
    }

    private CalendarView getCalendarView(Calendar date) {
        int viewId = date.get(Calendar.YEAR) * 100 + date.get(Calendar.MONTH);
        CalendarView calendarView = (CalendarView) findViewById(viewId);
        return calendarView;
    }

    class CalendarPagerAdapter extends FragmentPagerAdapter implements
            ViewPager.OnPageChangeListener {
        FragmentManager fragmentManager;

        public CalendarPagerAdapter(FragmentManager fm, ViewPager viewPager) {
            super(fm);
            viewPager.setAdapter(this);
            viewPager.addOnPageChangeListener(this);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar date = Calendar.getInstance();
            date.set(1970, Calendar.JANUARY, 1);
            date.add(Calendar.MONTH, position);
            return CalendarFragment.newInstance(date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.MONTH), date.get(Calendar.YEAR));
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
            mMonthOffset = position;
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(0);
            date.add(Calendar.MONTH, position - 1);
            CalendarView lastMonth = getCalendarView(date);
            date.add(Calendar.MONTH, 2);
            CalendarView nextMonth = getCalendarView(date);
            date.add(Calendar.MONTH, -1);
            CalendarView thisMonth = getCalendarView(date);
            if(lastMonth != null) {
                lastMonth.setSelectedDay(-1);
            }
            if(nextMonth != null) {
                nextMonth.setSelectedDay(-1);
            }
            if(thisMonth != null) {
                mSelectedDay = thisMonth.setSelectedDay(mSelectedDay);
            }
            mLabelMonthName.setText(
                    date.get(Calendar.YEAR) + "年" + (date.get(Calendar.MONTH) + 1) + "月");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
