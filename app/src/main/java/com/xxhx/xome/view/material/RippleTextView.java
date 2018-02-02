package com.xxhx.xome.view.material;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xxhx on 2017/4/6.
 */

public class RippleTextView extends TextView {
    public RippleTextView(Context context) {
        super(context);
    }

    public RippleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setBackground(Drawable background) {
        // Attribute array
        int[] attrs = new int[] { android.R.attr.selectableItemBackground };

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs);

        // Drawable held by attribute 'selectableItemBackground' is at index '0'
        Drawable d = a.getDrawable(0);

        a.recycle();

        LayerDrawable ld = new LayerDrawable(new Drawable[] {

                background,

                // Drawable from attribute
                d });

        // Set the background to 'ld'
        super.setBackground(ld);
    }
}
