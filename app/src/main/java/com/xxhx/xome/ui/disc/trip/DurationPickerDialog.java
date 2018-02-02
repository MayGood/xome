package com.xxhx.xome.ui.disc.trip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.xxhx.xome.R;

/**
 * Created by xxhx on 2018/1/25.
 */

public class DurationPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private EditText mHourEditor;
    private EditText mMinuteEditor;

    private final OnDurationSetListener mDurationSetListener;

    public interface OnDurationSetListener {
        void onDurationSet(int durationInMinutes);
    }

    protected DurationPickerDialog(Context context, OnDurationSetListener listener) {
        super(context);

        mDurationSetListener = listener;

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_duration_picker, null);
        mHourEditor = view.findViewById(R.id.hour);
        mMinuteEditor = view.findViewById(R.id.minute);
        setView(view);

        setButton(BUTTON_POSITIVE, "确定", this);
        setButton(BUTTON_NEGATIVE, "取消", this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mDurationSetListener != null) {
                    int duration = 0;
                    try {
                        int hour = Integer.parseInt(mHourEditor.getText().toString());
                        duration += hour * 60;
                    } catch (Exception e) {}
                    try {
                        int minute = Integer.parseInt(mMinuteEditor.getText().toString());
                        duration += minute;
                    } catch (Exception e) {}
                    mDurationSetListener.onDurationSet(duration);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }
}
