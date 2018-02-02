package com.xxhx.xome.ui.disc.checkin.view;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.CheckinDao;
import com.xxhx.xome.ui.disc.checkin.CheckinActivity;
import com.xxhx.xome.ui.disc.checkin.data.Checkin;
import com.xxhx.xome.ui.disc.checkin.data.DayType;
import com.xxhx.xome.ui.disc.checkin.util.WorkdayUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xxhx on 2017/8/1.
 */

public class CalendarView extends GridView {

    private CheckinActivity mActivity;
    private MyAdapter mAdapter;

    private Calendar mFirstDay;
    private int mFirstDayPosition;
    private int mLastDayPosition;
    private Map<Long, List<Checkin>> mCheckinMap;

    private int mSelectedPosition = -1;
    private ViewHolder mSelectedHolder;
    private Calendar mSelectedCalendar;

    private OnDateSelectedListener mOnDateSelectedListener;

    public interface OnDateSelectedListener {
        void onDateSelected(int position, Calendar date, List<Checkin> checkins);
    }

    public CalendarView(CheckinActivity activity) {
        super(activity.getBaseContext());
        mActivity = activity;
    }

    public void init(int year, int month, int dayOfMonth) {
        mFirstDay = Calendar.getInstance();
        mFirstDay.set(year, month, dayOfMonth);
        mFirstDayPosition = mFirstDay.get(Calendar.DAY_OF_WEEK) - 1;
        mLastDayPosition = mFirstDayPosition + mFirstDay.getActualMaximum(Calendar.DAY_OF_MONTH);

        initCheckinMap();

        setSelector(R.color.transparent);
        setNumColumns(7);
        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mSelectedHolder != null) {
                    updateHolderView(mSelectedPosition, mSelectedCalendar, mSelectedHolder, false);
                }
                mSelectedCalendar = (Calendar) parent.getItemAtPosition(position);
                mSelectedPosition = position;
                if(view != null) {
                    mSelectedHolder = (ViewHolder) view.getTag();
                    updateHolderView(position, mSelectedCalendar, mSelectedHolder, true);
                }
                if(mOnDateSelectedListener != null) {
                    long date = mSelectedCalendar.get(Calendar.YEAR) * 10000
                            + mSelectedCalendar.get(Calendar.MONTH) * 100
                            + mSelectedCalendar.get(Calendar.DAY_OF_MONTH);
                    mOnDateSelectedListener.onDateSelected(position, mSelectedCalendar, mCheckinMap.get(date));
                }
                if(position < mFirstDayPosition) {
                    mActivity.navigation(-1);
                }
                else if(position >= mLastDayPosition) {
                    mActivity.navigation(1);
                }
            }
        });

        mAdapter = new MyAdapter();
        setAdapter(mAdapter);
    }

    private void initCheckinMap() {
        if (mCheckinMap == null) {
            mCheckinMap = new HashMap<Long, List<Checkin>>();
        } else {
            mCheckinMap.clear();
        }

        int size = 7 * mFirstDay.getActualMaximum(Calendar.WEEK_OF_MONTH);
        Calendar calendar = (Calendar) mFirstDay.clone();
        calendar.add(Calendar.DAY_OF_MONTH, -mFirstDayPosition);
        long start = calendar.get(Calendar.YEAR) * 10000
                + calendar.get(Calendar.MONTH) * 100
                + calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, size);
        long end = calendar.get(Calendar.YEAR) * 10000
                + calendar.get(Calendar.MONTH) * 100
                + calendar.get(Calendar.DAY_OF_MONTH);

        List<Checkin> data = App.getInstance()
                .getDaoSession()
                .getCheckinDao()
                .queryBuilder()
                .where(CheckinDao.Properties.Date.ge(start), CheckinDao.Properties.Date.lt(end))
                .orderAsc(CheckinDao.Properties.Date)
                .list();

        for(Checkin checkin : data) {
            List<Checkin> checkins = mCheckinMap.get(checkin.getDate());
            if(checkins == null) {
                checkins = new ArrayList<Checkin>();
                checkins.add(checkin);
                mCheckinMap.put(checkin.getDate(), checkins);
            }
            else {
                checkins.add(checkin);
            }
        }
    }

    public void addCheckin(Checkin checkin) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkin.getTime());
        long date = calendar.get(Calendar.YEAR) * 10000
                + calendar.get(Calendar.MONTH) * 100
                + calendar.get(Calendar.DAY_OF_MONTH);
        List<Checkin> checkins = mCheckinMap.get(date);
        if(checkins == null) {
            checkins = new ArrayList<Checkin>();
            checkins.add(checkin);
            mCheckinMap.put(date, checkins);
        }
        else {
            checkins.add(checkin);
        }
        mAdapter.notifyDataSetChanged();
    }

    public int setSelectedDay(int day) {
        if(day < 0) {
            updateHolderView(mSelectedPosition, mSelectedCalendar, mSelectedHolder, false);
            mSelectedPosition = -1;
            mSelectedHolder = null;
            mSelectedCalendar = null;
            return day;
        }
        int position = mFirstDayPosition + day - 1;
        if(position >= mLastDayPosition) {
            position = mLastDayPosition - 1;
        }
        try {
            performItemClick(getChildAt(position), position, getItemIdAtPosition(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (position - mFirstDayPosition + 1);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mOnDateSelectedListener = listener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        throw new IllegalArgumentException("CalendarView中禁止使用setOnItemClickListener方法");
    }

    private void updateHolderView(int position, Calendar calendar, ViewHolder holder, boolean isSelected) {
        if(position < 0 || holder == null) return;
        if(position < mFirstDayPosition) {
            holder.date.setTextColor(Color.LTGRAY);
        }
        else if(position >= mLastDayPosition) {
            holder.date.setTextColor(Color.LTGRAY);
        }
        else {
            holder.date.setTextColor(Color.BLACK);
        }
        if(isSelected) {
            if(mSelectedHolder == null) {
                mSelectedCalendar = calendar;
                mSelectedPosition = position;
                mSelectedHolder = holder;
            }
            if(DateUtils.isToday(calendar.getTimeInMillis())) {
                holder.date.setTextColor(Color.WHITE);
                holder.date.setBackgroundResource(R.drawable.bg_calendar_today);
            }
            else {
                holder.date.setBackgroundResource(R.drawable.bg_calendar_selected);
            }
        }
        else {
            holder.date.setBackground(null);
        }

        DayType dayType = WorkdayUtil.getDayType(calendar.getTimeInMillis());
        holder.workday.setVisibility(GONE);
        holder.freeday.setVisibility(GONE);
        if(dayType == DayType.Workday) {
            holder.workday.setVisibility(VISIBLE);
        }
        else if(dayType == DayType.Freeday) {
            holder.freeday.setVisibility(VISIBLE);
        }

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        holder.date.setText(String.valueOf(dayOfMonth));

        long date = calendar.get(Calendar.YEAR) * 10000 + calendar.get(Calendar.MONTH) * 100 + calendar.get(Calendar.DAY_OF_MONTH);
        List<Checkin> checkins = mCheckinMap.get(date);
        if(checkins != null && checkins.size() > 0) {
            int flag = 0;
            for (Checkin checkin : checkins) {
                if(checkin.getCheckout()) {
                    flag |= 1;
                }
                else {
                    flag |= 2;
                }
            }
            if(flag == 3) {
                holder.status.setImageResource(R.drawable.indicator_in_out);
            }
            else if(flag == 2) {
                holder.status.setImageResource(R.drawable.indicator_in);
            }
            else {
                holder.status.setImageResource(R.drawable.indicator_out);
            }
            holder.status.setVisibility(VISIBLE);
        }
        else {
            holder.status.setVisibility(GONE);
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 7 * mFirstDay.getActualMaximum(Calendar.WEEK_OF_MONTH);
        }

        @Override
        public Calendar getItem(int position) {
            Calendar calendar = (Calendar) mFirstDay.clone();
            calendar.add(Calendar.DAY_OF_MONTH, position - mFirstDayPosition);
            return calendar;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private boolean isSameDay(Calendar c0, Calendar c1) {
            if (c0.get(Calendar.YEAR) == c1.get(Calendar.YEAR) && c0.get(Calendar.MONTH) == c1.get(
                    Calendar.MONTH) && c0.get(Calendar.DAY_OF_MONTH) == c1.get(
                    Calendar.DAY_OF_MONTH)) {
                return true;
            }
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_calendar, parent, false);
                holder = new ViewHolder();
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.freeday = convertView.findViewById(R.id.freeday);
                holder.workday = convertView.findViewById(R.id.workday);
                holder.status = (ImageView) convertView.findViewById(R.id.status);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Calendar item = getItem(position);
            //if (mSelectedHolder == null && (mSelectedPosition > 0 || DateUtils.isToday(
            //        holder.calendar.getTimeInMillis()))) {
            //    updateHolderView(position, holder, true);
            //    if(mOnDateSelectedListener != null) {
            //        long date = item.get(Calendar.YEAR) * 10000
            //                + item.get(Calendar.MONTH) * 100
            //                + item.get(Calendar.DAY_OF_MONTH);
            //        mOnDateSelectedListener.onDateSelected(position, item, mCheckinMap.get(date));
            //    }
            //} else
            if (mSelectedCalendar != null && isSameDay(mSelectedCalendar, item)) {
                updateHolderView(position, item, holder, true);
            } else {
                updateHolderView(position, item, holder, false);
            }
            return convertView;
        }
    }

    class ViewHolder {
        //Calendar calendar;
        TextView date;
        View freeday;
        View workday;
        ImageView status;
    }
}
