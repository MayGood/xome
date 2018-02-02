package com.xxhx.xome.ui.disc.wealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.TurnoverDao;
import com.xxhx.xome.data.WealthAccountDao;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.disc.wealth.TurnoverSortListActivity;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import com.xxhx.xome.ui.disc.wealth.util.MyAxisValueFormatter;
import com.xxhx.xome.ui.disc.wealth.util.MyValueFormatter;
import com.xxhx.xome.ui.disc.wealth.view.WealthPieMarkerView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by xxhx on 2017/7/13.
 */

public class OverviewListAdapter extends ArrayAdapter<TurnoverType> {

    private DecimalFormat mFormat = new DecimalFormat("#,####");
    private ArrayList<Integer> mColors;

    private final Comparator fWealthComparator = new Comparator<WealthAccount>() {
        @Override
        public int compare(WealthAccount lhs, WealthAccount rhs) {
            long absL = Math.abs(lhs.getBalanceInFens());
            long absR = Math.abs(rhs.getBalanceInFens());
            if(absL < absR) return 1;
            else if(absL > absR) return -1;
            return 0;
        }
    };

    public OverviewListAdapter(Context context, List<TurnoverType> objects) {
        super(context, 0, objects);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 0;
        else return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private void initFirstView(FirstViewHolder holder) {
        List<WealthAccount> wealthAccounts = App.getInstance().getDaoSession().getWealthAccountDao()
                .queryBuilder()
                .orderDesc(WealthAccountDao.Properties.Type)
                .orderAsc(WealthAccountDao.Properties.AddedTime)
                .list();
        List<WealthAccount> positiveAccounts = new ArrayList<WealthAccount>();
        List<WealthAccount> negativeAccounts = new ArrayList<WealthAccount>();
        long total = 0;
        if(wealthAccounts != null) {
            for(WealthAccount wealthAccount : wealthAccounts) {
                total += wealthAccount.getBalanceInFens();
                if(wealthAccount.getBalanceInFens() >= 0) {
                    positiveAccounts.add(wealthAccount);
                }
                else {
                    negativeAccounts.add(wealthAccount);
                }
            }
        }

        if(total >= 0) {
            holder.total.setTextColor(getContext().getResources().getColor(R.color.positiveWealth));
        }
        else {
            holder.total.setTextColor(getContext().getResources().getColor(R.color.negativeWealth));
        }
        holder.total.setText(String.format("%s.%02d", mFormat.format(total / 100), Math.abs(total) % 100));

        mColors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            mColors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            mColors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            mColors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            mColors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            mColors.add(c);

        mColors.add(ColorTemplate.getHoloBlue());

        initPositiveView(holder.pChart, positiveAccounts);
        initNegativeView(holder.nChart, negativeAccounts);
    }

    private void initPositiveView(PieChart chart, List<WealthAccount> wealthAccounts) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        //mPositivePieView.setCenterText(generateCenterSpannableText());

        chart.setDrawEntryLabels(false);
        //mPositivePieView.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        long total = 0;
        Collections.sort(wealthAccounts, fWealthComparator);
        for(WealthAccount wealthAccount : wealthAccounts) {
            entries.add(new PieEntry((float) (wealthAccount.getBalanceInFens() / 100.0)));
            total += wealthAccount.getBalanceInFens();
        }

        chart.setCenterText(generateCenterSpannableText(total));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0; i < entries.size(); i++) {
            colors.add(mColors.remove(0));
        }
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        //dataSet.setSelectionShift(0f);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        WealthPieMarkerView mv = new WealthPieMarkerView(getContext());
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private void initNegativeView(PieChart chart, List<WealthAccount> wealthAccounts) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawEntryLabels(false);
        //mNegativePieView.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        long total = 0;
        Collections.sort(wealthAccounts, fWealthComparator);
        for(WealthAccount wealthAccount : wealthAccounts) {
            entries.add(new PieEntry((float) (-wealthAccount.getBalanceInFens() / 100.0)));
            total += wealthAccount.getBalanceInFens();
        }

        chart.setCenterText(generateCenterSpannableText(total));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(mColors);
        dataSet.setDrawValues(false);
        //dataSet.setSelectionShift(0f);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        WealthPieMarkerView mv = new WealthPieMarkerView(getContext());
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private Spannable generateCenterSpannableText(long total) {

        SpannableStringBuilder s = new SpannableStringBuilder();
        if (total >= 0) {
            s.append(String.format("%s.%02d", mFormat.format(Math.abs(total) / 100),
                    Math.abs(total) % 100));
            s.setSpan(new RelativeSizeSpan(1.6f), 0, s.length() - 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(
                            getContext().getResources().getColor(R.color.positiveWealth)), 0,
                    s.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(1.2f), s.length() - 3, s.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), s.length() - 3, s.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            s.append(String.format("%s.%02d", mFormat.format(Math.abs(total) / 100),
                    Math.abs(total) % 100));
            s.setSpan(new RelativeSizeSpan(1.2f), 0, s.length() - 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(
                            getContext().getResources().getColor(R.color.negativeWealth)), 0,
                    s.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(0.8f), s.length() - 3, s.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), s.length() - 3, s.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    private void initContentView(ViewHolder holder, TurnoverType turnoverType) {
        if(turnoverType.isWealthIn()) {
            holder.title.setTextColor(getContext().getResources().getColor(R.color.positiveWealth));
        }
        else {
            holder.title.setTextColor(getContext().getResources().getColor(R.color.negativeWealth));
        }
        if(turnoverType == TurnoverType.TRANSFER) {
            holder.title.setText("收支");
            holder.title.setTextColor(Color.BLACK);
        }
        else {
            holder.title.setText(turnoverType.getType());
        }

        holder.chart.setDrawBarShadow(false);
        holder.chart.setDrawValueAboveBar(true);

        holder.chart.getDescription().setEnabled(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new MyAxisValueFormatter(MyAxisValueFormatter.Mode.MONTH));

        IAxisValueFormatter custom = new MyAxisValueFormatter(MyAxisValueFormatter.Mode.INT);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setEnabled(false);

        holder.chart.getLegend().setEnabled(false);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -6);
        List<Turnover> turnovers;
        QueryBuilder<Turnover> builder = App.getInstance()
                .getDaoSession()
                .getTurnoverDao()
                .queryBuilder()
                .where(TurnoverDao.Properties.Time.ge(calendar.getTime()));
        if(turnoverType == TurnoverType.TRANSFER) {
            turnovers = builder.list();
        }
        else {
            turnovers = builder.where(TurnoverDao.Properties.TurnoverType.eq(turnoverType)).list();
        }

        int startMonth = calendar.get(Calendar.MONTH);
        long[] dividers = new long[7];
        dividers[0] = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        dividers[1] = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        dividers[2] = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        dividers[3] = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        dividers[4] = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        dividers[5] = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        dividers[6] = calendar.getTimeInMillis();

        long[] amounts = new long[7];
        for(Turnover turnover : turnovers) {
            if(turnover.getTime().getTime() >= dividers[6]) {
                amounts[6] += turnover.getAmountInFens();
            }
            else if(turnover.getTime().getTime() >= dividers[5]) {
                amounts[5] += turnover.getAmountInFens();
            }
            else if(turnover.getTime().getTime() >= dividers[4]) {
                amounts[4] += turnover.getAmountInFens();
            }
            else if(turnover.getTime().getTime() >= dividers[3]) {
                amounts[3] += turnover.getAmountInFens();
            }
            else if(turnover.getTime().getTime() >= dividers[2]) {
                amounts[2] += turnover.getAmountInFens();
            }
            else if(turnover.getTime().getTime() >= dividers[1]) {
                amounts[1] += turnover.getAmountInFens();
            }
            else if(turnover.getTime().getTime() >= dividers[0]) {
                amounts[0] += turnover.getAmountInFens();
            }
        }

        if(turnoverType.isWealthIn()) {
            float minValue = 0;
            for (int i = 0; i < amounts.length; i++) {
                float value = amounts[i] / 100.0f;
                if(minValue > value) minValue = value;
                yVals1.add(new BarEntry(startMonth++, value));
            }
            if(minValue < 0) {
                minValue = ((long) minValue - 9999) / 10000 * 10000;
            }
            leftAxis.setAxisMinimum(minValue);
        }
        else {
            for (int i = 0; i < amounts.length; i++) {
                yVals1.add(new BarEntry(startMonth++, Math.abs(amounts[i]) / 100.0f));
            }
            leftAxis.setAxisMinimum(0);
        }

        BarDataSet set1;

        set1 = new BarDataSet(yVals1, "The year 2017");

        set1.setDrawIcons(false);

        List<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            if(colors.size() == 7) break;
            colors.add(0, c);
        }
        set1.setColors(colors);
        set1.setHighLightAlpha(0);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new MyValueFormatter());

        holder.chart.setData(data);
        holder.chart.animateY(900);
    }

    private View getFirstView(View convertView, ViewGroup parent) {
        FirstViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_wealth_overview, parent, false);
            holder = new FirstViewHolder();
            holder.pChart = (PieChart) convertView.findViewById(R.id.pie_p);
            holder.nChart = (PieChart) convertView.findViewById(R.id.pie_n);
            holder.total = (TextView) convertView.findViewById(R.id.total);
            convertView.setTag(holder);
        }
        else {
            holder = (FirstViewHolder) convertView.getTag();
        }
        initFirstView(holder);
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0) return getFirstView(convertView, parent);

        final TurnoverType turnoverType = getItem(position - 1);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_wealth_overview_type, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);
            convertView.setTag(holder);
            //holder.chart.setOnTouchListener(null);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        initContentView(holder, turnoverType);
        //holder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
        //    @Override
        //    public void onValueSelected(Entry e, Highlight h) {
        //        Intent intent = new Intent(getContext(), TurnoverSortListActivity.class);
        //        intent.putExtra(TurnoverSortListActivity.EXTRA_TYPE, turnoverType.name());
        //        intent.putExtra(TurnoverSortListActivity.EXTRA_MONTH, (int) e.getX());
        //        getContext().startActivity(intent);
        //    }
        //
        //    @Override
        //    public void onNothingSelected() {
        //
        //    }
        //});
        return convertView;
    }

    class FirstViewHolder {
        PieChart pChart;
        PieChart nChart;
        TextView total;
    }

    class ViewHolder {
        TextView title;
        BarChart chart;
    }
}
