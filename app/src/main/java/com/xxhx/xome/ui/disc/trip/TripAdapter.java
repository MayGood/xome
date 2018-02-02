package com.xxhx.xome.ui.disc.trip;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.disc.trip.data.Station;
import com.xxhx.xome.ui.disc.trip.data.TimeTable;
import com.xxhx.xome.ui.disc.trip.data.Trip;
import com.xxhx.xome.util.CommonUtil;
import com.xxhx.xome.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xxhx.xome.R.id.time_leave;

/**
 * Created by xxhx on 2017/5/9.
 */

public class TripAdapter extends ArrayAdapter<Trip> {
    private Map<Integer, Boolean> mDefaultRemarkMap;
    private List<Trip> mTrips;

    public TripAdapter(Context context, List<Trip> trips) {
        super(context, R.layout.list_item_trip, trips);
        mDefaultRemarkMap = new HashMap<Integer, Boolean>();
        mTrips = new ArrayList<Trip>();
        mTrips.addAll(trips);
    }

    private void showTimeTable(Trip trip) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_content_time_table, null);
        ListView listView = (ListView) contentView.findViewById(R.id.list);
        AssetManager assets = getContext().getAssets();
        InputStream is = null;
        try {
            is = assets.open("trips/" + trip.getNo().toLowerCase() + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(is == null) {
            ToastHelper.toast("暂无该车数据");
        }
        else {
            String ttStr = StringUtil.inputStreamToString(is);
            if(ttStr != null) {
                Gson gson = new Gson();
                TimeTable timeTable = gson.fromJson(ttStr, TimeTable.class);
                StationAdapter adapter = new StationAdapter(getContext(), trip.getStationFrom(), trip.getStationTo(), timeTable.getStations());
                listView.setAdapter(adapter);
                listView.setSelection(adapter.getFromIndex());
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(contentView).create();
                dialog.show();
            }
        }
    }

    @Override
    public int getCount() {
        return mTrips.size();
    }

    @Nullable
    @Override
    public Trip getItem(int position) {
        return mTrips.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trip, parent, false);
            holder = new ViewHolder();
            holder.front = convertView.findViewById(R.id.front);
            holder.back = convertView.findViewById(R.id.back);
            holder.remark = (TextView) convertView.findViewById(R.id.remark);
            holder.remarkEdit = convertView.findViewById(R.id.remark_edit);
            holder.remarkDelete = convertView.findViewById(R.id.remark_delete);
            holder.type = (ImageView) convertView.findViewById(R.id.type);
            holder.no = (TextView) convertView.findViewById(R.id.no);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.timeTable = convertView.findViewById(R.id.time_table);
            holder.stationFrom = (TextView) convertView.findViewById(R.id.station_from);
            holder.timeStart = (TextView) convertView.findViewById(R.id.time_start);
            holder.stationTo = (TextView) convertView.findViewById(R.id.station_to);
            holder.timeArrive = (TextView) convertView.findViewById(R.id.time_arrive);
            holder.jumpDays = (TextView) convertView.findViewById(R.id.jump_days);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.seat = (TextView) convertView.findViewById(R.id.seat);
            holder.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Trip trip = getItem(position);
        switch (trip.getType()) {
            case Railway:
                holder.type.setImageResource(R.drawable.ic_trip_railway);
                holder.timeTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTimeTable(trip);
                    }
                });
                holder.timeTable.setVisibility(View.VISIBLE);
                break;
            case Flight:
                holder.type.setImageResource(R.drawable.ic_trip_flight);
                holder.timeTable.setVisibility(View.GONE);
                break;
        }
        holder.no.setText(trip.getNo());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trip.getTime());
        holder.date.setText(String.format("%02d月%02d日", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        holder.stationFrom.setText(trip.getStationFrom());
        holder.timeStart.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        calendar.add(Calendar.MINUTE, trip.getDurationInMinutes());
        holder.stationTo.setText(trip.getStationTo());
        holder.timeArrive.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        long jumpDays = CommonUtil.getRelativeDays(calendar.getTimeInMillis(), trip.getTime().getTime());
        if(jumpDays != 0) {
            holder.jumpDays.setText(String.format("%+d", jumpDays));
            holder.jumpDays.setVisibility(View.VISIBLE);
        }
        else {
            holder.jumpDays.setVisibility(View.GONE);
        }
        if(trip.getDurationInMinutes() > 1440) {
            int hours = trip.getDurationInMinutes() / 60;
            holder.duration.setText(String.format("%d天%d小时%d分", hours / 24, hours % 24 , trip.getDurationInMinutes() % 60));
        }
        else if(trip.getDurationInMinutes() > 60) {
            holder.duration.setText(String.format("%d小时%d分", trip.getDurationInMinutes() / 60, trip.getDurationInMinutes() % 60));
        }
        else {
            holder.duration.setText(String.format("%d分", trip.getDurationInMinutes()));
        }
        holder.seat.setText(trip.getSeat());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTripDeleteDialog(trip);
            }
        });
        String remark = trip.getRemark();
        if(TextUtils.isEmpty(remark)) {
            holder.remark.setText(R.string.no_remark);
            holder.remarkDelete.setVisibility(View.GONE);
        }
        else {
            holder.remark.setText(remark);
            holder.remarkDelete.setVisibility(View.VISIBLE);
        }
        holder.remarkEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemarkEditDialog(trip);
            }
        });
        holder.remarkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemarkDeleteDialog(trip);
            }
        });
        holder.front.setRotationY(0);
        holder.back.setRotationY(0);
        if(mDefaultRemarkMap.containsKey(position) && mDefaultRemarkMap.get(position)) {
            holder.front.setVisibility(View.INVISIBLE);
            holder.back.setVisibility(View.VISIBLE);
        }
        else {
            holder.back.setVisibility(View.INVISIBLE);
            holder.front.setVisibility(View.VISIBLE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFrontWithBack(position, v);
            }
        });
        return convertView;
    }

    private void showTripDeleteDialog(final Trip trip) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确认删除该行程？")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTrips.remove(trip);
                        App.getInstance().getDaoSession().getTripDao().delete(trip);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void showRemarkEditDialog(final Trip trip) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_single_editor, null);
        final EditText editor = (EditText) view.findViewById(R.id.edit);
        editor.setHint("行程备注");
        if(!TextUtils.isEmpty(trip.getRemark())) {
            editor.setText(trip.getRemark());
            editor.setSelection(editor.getText().length());
        }
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trip.setRemark(editor.getText().toString());
                        App.getInstance().getDaoSession().getTripDao().update(trip);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void showRemarkDeleteDialog(final Trip trip) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确认删除？")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trip.setRemark(null);
                        App.getInstance().getDaoSession().getTripDao().update(trip);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void swapFrontWithBack(int position, View view) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        ObjectAnimator animator1, animator2;
        if(holder.front.getVisibility() == View.VISIBLE) {
            mDefaultRemarkMap.put(position, true);
            holder.back.setVisibility(View.INVISIBLE);
            animator1 = ObjectAnimator.ofFloat(holder.front, "rotationY", 0, -90);
            animator1.setDuration(200);
            animator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    holder.front.setVisibility(View.INVISIBLE);
                    holder.back.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            animator2 = ObjectAnimator.ofFloat(holder.back, "rotationY", 90, 0);
            animator2.setDuration(200);
        }
        else {
            mDefaultRemarkMap.put(position, false);
            holder.back.setVisibility(View.VISIBLE);
            animator1 = ObjectAnimator.ofFloat(holder.back, "rotationY", 0, -90);
            animator1.setDuration(200);
            animator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    holder.back.setVisibility(View.INVISIBLE);
                    holder.front.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            animator2 = ObjectAnimator.ofFloat(holder.front, "rotationY", 90, 0);
            animator2.setDuration(200);
        }
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator1, animator2);
        set.start();
    }

    class ViewHolder {
        View front;
        View back;
        TextView remark;
        View remarkEdit;
        View remarkDelete;
        ImageView type;
        TextView no;
        TextView date;
        View timeTable;
        TextView stationFrom;
        TextView timeStart;
        TextView stationTo;
        TextView timeArrive;
        TextView jumpDays;
        TextView duration;
        TextView seat;
        View delete;
    }

    class StationAdapter extends ArrayAdapter<Station> {
        private int fromIndex;
        private int toIndex;

        public StationAdapter(Context context, String from, String to, List<Station> objects) {
            super(context, R.layout.list_item_station, objects);
            fromIndex = 0;
            toIndex = objects.size() - 1;
            int i = 0;
            while(i < objects.size()) {
                Station station = objects.get(i);
                if(station.getName().equals(from)) {
                    fromIndex = i++;
                    break;
                }
                i++;
            }
            while(i < objects.size()) {
                Station station = objects.get(i);
                if(station.getName().equals(to)) {
                    toIndex = i++;
                    break;
                }
                i++;
            }
        }

        public int getFromIndex() {
            return fromIndex;
        }

        public int getToIndex() {
            return toIndex;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StationViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_station, parent, false);
                holder = new StationViewHolder();
                holder.index = (TextView) convertView.findViewById(R.id.index);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.timeArrive = (TextView) convertView.findViewById(R.id.time_arrive);
                holder.timeLeave = (TextView) convertView.findViewById(time_leave);
                holder.stay = (TextView) convertView.findViewById(R.id.stay);
                convertView.setTag(holder);
            }
            else {
                holder = (StationViewHolder) convertView.getTag();
            }
            Station station = getItem(position);
            holder.index.setText(String.format("%02d", position));
            holder.name.setText(station.getName());
            holder.timeArrive.setText(station.getArriveTime());
            holder.timeLeave.setText(station.getLeaveTime());
            if(station.getStayMinutes() == 0) {
                holder.stay.setText("--");
            }
            else {
                holder.stay.setText(String.format("%d分钟", station.getStayMinutes()));
            }
            if(position < fromIndex || position > toIndex) {
                convertView.setBackgroundColor(Color.parseColor("#66D3D3D3"));
            }
            else if(position == fromIndex || position == toIndex) {
                convertView.setBackgroundColor(Color.parseColor("#3300A4EF"));
            }
            else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }
    }

    class StationViewHolder {
        TextView index;
        TextView name;
        TextView timeArrive;
        TextView timeLeave;
        TextView stay;
    }
}
