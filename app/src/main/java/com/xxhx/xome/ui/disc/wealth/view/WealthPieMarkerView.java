package com.xxhx.xome.ui.disc.wealth.view;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.xxhx.xome.R;
import java.text.DecimalFormat;

/**
 * Created by xxhx on 2017/7/12.
 */

public class WealthPieMarkerView extends MarkerView {

    private final DecimalFormat mFormat = new DecimalFormat("#,####.00");
    private TextView mAmountText;

    public WealthPieMarkerView(Context context) {
        super(context, R.layout.marker_wealth_pie);

        mAmountText = (TextView) findViewById(R.id.amount);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mAmountText.setText(mFormat.format(e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(getHeight() / 2));
    }
}
