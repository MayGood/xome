package com.xxhx.xome.ui.disc.trip;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.trip.data.Trip;
import com.xxhx.xome.ui.disc.trip.data.TripType;
import java.io.IOException;
import java.util.Calendar;

public class NewTripActivity extends BaseActivity {
    public static final String EXTRA_TYPE = "extra_type";

    public static final int TYPE_RAILWAY = 0;
    public static final int TYPE_FLIGHT = 1;

    private int mType;

    private EditText mNoEditor;
    private TextView mTextDate;
    private EditText mSeatEditor;
    private EditText mFromEditor;
    private EditText mToEditor;
    private TextView mTextTime;
    private TextView mTextDuration;

    private int mYear, mMonth, mDayOfMonth;
    private int mHourOfDay, mMinute;
    private int mDuration;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mType = getIntent().getIntExtra(EXTRA_TYPE, TYPE_RAILWAY);
        if(mType == TYPE_FLIGHT) {
            ((ViewStub) findViewById(R.id.stub_flight)).inflate();
        }
        else {
            ((ViewStub) findViewById(R.id.stub_railway)).inflate();
        }

        mNoEditor = (EditText) findViewById(R.id.no);
        mTextDate = (TextView) findViewById(R.id.date);
        mSeatEditor = (EditText) findViewById(R.id.seat);
        mFromEditor = (EditText) findViewById(R.id.station_from);
        mToEditor = (EditText) findViewById(R.id.station_to);
        mTextTime = (TextView) findViewById(R.id.time_start);
        mTextDuration = (TextView) findViewById(R.id.duration);

        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        mHourOfDay = now.get(Calendar.HOUR_OF_DAY);
        mMinute = now.get(Calendar.MINUTE);
        mTextDate.setText(String.format("%04d-%02d-%02d", mYear, mMonth + 1, mDayOfMonth));
        mTextTime.setText(String.format("%02d:%02d", mHourOfDay, mMinute));
    }

    public void chooseDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDayOfMonth = dayOfMonth;
                mTextDate.setText(String.format("%04d-%02d-%02d", mYear, mMonth + 1, mDayOfMonth));
            }
        }, mYear, mMonth, mDayOfMonth);
        dialog.show();
    }

    public void chooseTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHourOfDay = hourOfDay;
                mMinute = minute;
                mTextTime.setText(String.format("%02d:%02d", mHourOfDay, mMinute));
            }
        }, mHourOfDay, mMinute, true);
        dialog.show();
    }

    public void inputDuration(View view) {
        DurationPickerDialog dialog = new DurationPickerDialog(this, new DurationPickerDialog.OnDurationSetListener() {
            @Override
            public void onDurationSet(int durationInMinutes) {
                mDuration = durationInMinutes;

                int hour = durationInMinutes / 60;
                int minute = durationInMinutes % 60;
                if (hour > 0) {
                    mTextDuration.setText(String.format("%d小时%02d分钟", hour, minute));
                }
                else {
                    mTextDuration.setText(String.format("%02d分钟", minute));
                }
            }
        });
        dialog.show();
    }

    public void confirm(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDayOfMonth, mHourOfDay, mMinute, 0);
        Trip trip = new Trip();
        if(mType == TYPE_FLIGHT) {
            trip.setType(TripType.Flight);
        }
        else {
            trip.setType(TripType.Railway);
        }
        trip.setNo(mNoEditor.getText().toString().toUpperCase());
        trip.setSeat(mSeatEditor.getText().toString());
        trip.setTime(calendar.getTime());
        trip.setDurationInMinutes(mDuration);
        trip.setStationFrom(mFromEditor.getText().toString());
        trip.setStationTo(mToEditor.getText().toString());
        App.getInstance().getDaoSession().getTripDao().insert(trip);
        finish();
    }

}
