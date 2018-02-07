package com.xxhx.xome.ui.disc.note.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by xxhx on 2018/1/14.
 */

public class NoteTextView extends AppCompatTextView {
    private String mAlignText;

    public NoteTextView(Context context) {
        super(context);
    }

    public NoteTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mAlignText != null) {
            updateTextDrawable();
        }
    }

    public void setAlignText(String text) {
        mAlignText = text;
        updateTextDrawable();
    }

    private void updateTextDrawable() {
        StringBuilder textBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(mAlignText)) {
            int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            String[] paras = mAlignText.split("\n");
            for (String para : paras) {
                textBuilder.append(getAlignedText(para, width));
            }
            setText(textBuilder.toString());
        }
    }

    private String getAlignedText(String text, int maxWidth) {
        if (TextUtils.isEmpty(text) || maxWidth <= 0) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        TextPaint paint = getPaint();
        int start = 0;
        int end;
        while (start < text.length()) {
            end = start;
            while (end < text.length()) {
                if (paint.measureText(text, start, end + 1) > maxWidth) {
                    break;
                }
                end++;
            }
            sb.append(text.substring(start, end)).append("\n");
            start = end;
        }

        return sb.toString();
    }

}
