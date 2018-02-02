package com.xxhx.xome.ui.disc.wealth.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.DecimalFormat;

/**
 * Created by xxhx on 2017/7/12.
 */

public class MyAxisValueFormatter implements IAxisValueFormatter {

    public enum Mode {
        MONTH, FLOAT, INT
    }

    private Mode mMode;
    private DecimalFormat mFormat;

    public MyAxisValueFormatter(Mode mode) {
        this.mMode = mode;
        switch (mMode) {
            case FLOAT:
                mFormat = new DecimalFormat("#,###0.00");
                break;
            case INT:
                mFormat = new DecimalFormat("#,###0");
                break;
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(mMode == null) {
            return String.format("%.2f", value);
        }
        switch (mMode) {
            case MONTH:
                return getFormattedValueInMonth((int) value);
            case FLOAT:
            case INT:
                return mFormat.format(value);
        }
        return mFormat.format(value);
    }

    public String getFormattedValueInMonth(int value) {
        return String.format("%d月", value % 12 + 1);
    }

}
