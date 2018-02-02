package com.xxhx.xome.ui.disc.todos.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xxhx on 2018/1/14.
 */

public class TodoTextView extends android.support.v7.widget.AppCompatTextView {
    public TodoTextView(Context context) {
        super(context);
    }

    public TodoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TodoTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDeleteLineEnabled(boolean enabled) {
        int flags = getPaintFlags();
        if (enabled) {
            flags |= Paint.STRIKE_THRU_TEXT_FLAG;
        }
        else {
            flags &= ~Paint.STRIKE_THRU_TEXT_FLAG;
        }
        setPaintFlags(flags);
    }

}
