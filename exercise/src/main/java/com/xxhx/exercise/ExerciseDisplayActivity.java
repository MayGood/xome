package com.xxhx.exercise;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.xxhx.exercise.data.DaoMaster;
import com.xxhx.exercise.data.DaoSession;
import com.xxhx.exercise.data.Exercise;
import com.xxhx.exercise.data.ExerciseDao;
import com.xxhx.exercise.data.ExerciseDbHelper;
import com.xxhx.exercise.view.CustomScrollView;
import com.xxhx.exercise.view.PercentageLayout;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDisplayActivity extends AppCompatActivity {

    private static final DateFormat sDataFormat = new SimpleDateFormat("M月dd日");
    private static final DateFormat sTimeFormat = new SimpleDateFormat("HH:mm");

    private Exercise mExercise;

    private View mPlaceHolder;
    private CustomScrollView mScrollView;
    private MapView mAmapView;
    private AMap mAmap;

    private TextView mDistanceView;
    private TextView mDateView;
    private TextView mTimeView;
    private TextView mMilecostView;
    private TextView mDurationView;
    private TextView mTotalStepView;

    private ViewGroup mMilecostGroup;

    private TextView mAverageStepView;
    private LineChart mStepChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_display);

        mScrollView = (CustomScrollView) findViewById(R.id.scroll);
        mPlaceHolder = findViewById(R.id.place_holder);
        mAmapView = (MapView) findViewById(R.id.map);

        mDistanceView = (TextView) findViewById(R.id.distance);
        mDateView = (TextView) findViewById(R.id.date);
        mTimeView = (TextView) findViewById(R.id.time);
        mMilecostView = (TextView) findViewById(R.id.milecost);
        mDurationView = (TextView) findViewById(R.id.duration);
        mTotalStepView = (TextView) findViewById(R.id.total_step);

        mMilecostGroup = (ViewGroup) findViewById(R.id.group_milecost);

        mAverageStepView = (TextView) findViewById(R.id.average_step);
        mStepChart = (LineChart) findViewById(R.id.step_chart);

        mPlaceHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mAmapView.onCreate(savedInstanceState);

        mAmap = mAmapView.getMap();

        try {
            String path = new File(getExternalFilesDir("db"), "exercise-db").getAbsolutePath();
            ExerciseDbHelper helper = new ExerciseDbHelper(this, path);
            DaoSession daoSession = new DaoMaster(helper.getWritableDb()).newSession();
            mExercise = daoSession.getExerciseDao().queryBuilder().orderDesc(
                    ExerciseDao.Properties.StartTime).limit(1).list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
            return;
        }
        initMapUi();
        initOverview();
        initMilecostUi();
        initStepCountUi();
    }

    private void initMapUi() {
        UiSettings settings = mAmap.getUiSettings();
        //缩放按钮
        settings.setZoomControlsEnabled(false);
        //指南针
        settings.setCompassEnabled(false);
        //比例尺控件
        settings.setScaleControlsEnabled(true);
        //旋转手势
        settings.setRotateGesturesEnabled(false);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mScrollView.setChildAlignBottom((int) (240 * displayMetrics.density));
    }

    private void drawMarkers(LatLng start, LatLng end) {
        MarkerOptions startMarker =
                new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start))
                        .position(start);
        MarkerOptions endMarker =
                new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end))
                        .position(end);
        mAmap.addMarker(startMarker);
        mAmap.addMarker(endMarker);
    }

    private void drawExercisePath() {
        int startColor = Color.parseColor("#ff536a");
        int middleColor = Color.parseColor("#25c78a");
        int endColor = Color.parseColor("#17c4ff");
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);
        int middleR = Color.red(middleColor);
        int middleG = Color.green(middleColor);
        int middleB = Color.blue(middleColor);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        List<Double> latitudes = mExercise.getPathLatitude();
        List<Double> longitudes = mExercise.getPathLongitude();
        List<Float> speeds = mExercise.getPathSpeed();
        if(latitudes == null || longitudes == null || speeds == null ||
                latitudes.size() != longitudes.size() || latitudes.size() != speeds.size()) {
            return;
        }
        int size = latitudes.size();
        List<LatLng> latLngs = new ArrayList<LatLng>();
        double minLatitude = latitudes.get(0);
        double minLongitude = longitudes.get(0);
        double maxLatitude = minLatitude;
        double maxLongitude = minLongitude;
        float maxSpeed = speeds.get(0);
        float minSpeed = 0;
        double lastLatitude = latitudes.get(0);
        double lastLongitude = longitudes.get(0);
        for(int i = 0; i < size; i++) {
            LatLng latLng;
            if(i == 0 || i == size - 1) {
                latLng = new LatLng(latitudes.get(i), longitudes.get(i));
            }
            else {
                latLng = new LatLng((lastLatitude + latitudes.get(i) + latitudes.get(i + 1)) / 3,
                        (lastLongitude + longitudes.get(i) + longitudes.get(i + 1)) / 3);
            }
            lastLatitude = latitudes.get(i);
            lastLongitude = longitudes.get(i);

            if(latLng.latitude < minLatitude) minLatitude = latLng.latitude;
            else if(latLng.latitude > maxLatitude) maxLatitude = latLng.latitude;
            if(latLng.longitude < minLongitude) minLongitude = latLng.longitude;
            else if(latLng.longitude > maxLongitude) maxLongitude = latLng.longitude;
            if(speeds.get(i) > maxSpeed) maxSpeed = speeds.get(i);
            else if(speeds.get(i) < minSpeed) minSpeed = speeds.get(i);
            latLngs.add(latLng);
        }
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngs).width(10).zIndex(10);
        float speedArange = maxSpeed - minSpeed;
        if(speedArange > 0) {
            List<Integer> colorValues = new ArrayList<Integer>();
            for(int i = 0; i < size; i++) {
                float p = (speeds.get(i) - minSpeed) / speedArange;
                if(p > 0.5) {
                    p = (p - 0.5f) / 0.5f;
                    int r = (int) (middleR * (1 - p) + endR * p);
                    int g = (int) (middleG * (1 - p) + endG * p);
                    int b = (int) (middleB * (1 - p) + endB * p);
                    colorValues.add(Color.rgb(r, g, b));
                }
                else {
                    p = p / 0.5f;
                    int r = (int) (startR * (1 - p) + middleR * p);
                    int g = (int) (startG * (1 - p) + middleG * p);
                    int b = (int) (startB * (1 - p) + middleB * p);
                    colorValues.add(Color.rgb(r, g, b));
                }
            }
            polylineOptions.useGradient(true);
            polylineOptions.colorValues(colorValues);
        }
        else {
            polylineOptions.color(Color.parseColor("#17c4ff"));
        }
        mAmap.addPolyline(polylineOptions);
        drawMarkers(latLngs.get(0), latLngs.get(size - 1));

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int horizontalPadding = (int) (48 * displayMetrics.density);
        int topPadding = (int) (48 * displayMetrics.density);
        int bottomPadding = (int) (288 * displayMetrics.density);
        //mAmap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(
        //                minLatitude, minLongitude), new LatLng(maxLatitude, maxLongitude)),
        //        horizontalPadding));
        mAmap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(
                new LatLngBounds(new LatLng(minLatitude, minLongitude),
                        new LatLng(maxLatitude, maxLongitude)), horizontalPadding,
                horizontalPadding, topPadding, bottomPadding));
    }

    private void initOverview() {
        mDistanceView.setText(String.format("%.02f", mExercise.getDistance() / 1000));
        mDateView.setText(sDataFormat.format(mExercise.getStartTime()));
        try {
            mTimeView.setText(String.format("%s - %s", sTimeFormat.format(mExercise.getStartTime()), sTimeFormat.format(mExercise.getEndTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long secsPerKm = (long) (mExercise.getDuration() / mExercise.getDistance());
        mMilecostView.setText(String.format("%02d\'%02d\"", secsPerKm / 60, secsPerKm % 60));
        long duration = mExercise.getDuration();
        mDurationView.setText(String.format("%02d:%02d:%02d", duration / (60 * 60 * 1000), (duration / (60 * 1000)) % 60, (duration / 1000) % 60));
        mTotalStepView.setText(Long.toString(mExercise.getStepCount()));
    }

    private void initMilecostUi() {
        List<Long> milecosts = new ArrayList<Long>();
        List<Long> millsPerKM = mExercise.getMillsPerKM();
        if(millsPerKM == null || millsPerKM.size() == 0) return;
        long maxMilecost = millsPerKM.get(0);
        long minMilecost = millsPerKM.get(0);
        milecosts.add(millsPerKM.get(0));
        for(int i = 1; i < millsPerKM.size(); i++) {
            long milecost = millsPerKM.get(i) - millsPerKM.get(i-1);
            if(milecost > maxMilecost) maxMilecost = milecost;
            if(milecost < minMilecost) minMilecost = milecost;
            milecosts.add(milecost);
        }

        long cost = 0;
        for(int i = 0; i < milecosts.size(); i++) {
            long milecost = milecosts.get(i);
            cost += milecost;

            View view = LayoutInflater.from(this).inflate(R.layout.list_item_milecost, null);
            View fastView = view.findViewById(R.id.fast);
            PercentageLayout percentageLayout = (PercentageLayout) view.findViewById(R.id.percentage);
            TextView mileView = (TextView) view.findViewById(R.id.mile);
            TextView milecostView = (TextView) view.findViewById(R.id.milecost);
            TextView costView = (TextView) view.findViewById(R.id.cost);

            if(milecost == minMilecost) fastView.setVisibility(View.VISIBLE);
            else fastView.setVisibility(View.GONE);
            percentageLayout.setPercentage(((float) milecost) / maxMilecost);
            mileView.setText("" + (i + 1));
            milecostView.setText(String.format("%02d\'%02d\"", milecost / 60000, (milecost / 1000) % 60));
            if(i == milecosts.size() - 1) {
                costView.setText(String.format("%02d:%02d:%02d", cost / (24 * 60 * 60000), (cost / 60000) % 60, (cost / 1000) % 60));
                costView.setVisibility(View.VISIBLE);
            }
            else {
                costView.setVisibility(View.GONE);
            }

            mMilecostGroup.addView(view);
        }
    }

    private void initStepCountUi() {
        if(mExercise.getDuration() > 0) {
            mAverageStepView.setText(Integer.toString(
                    (int) (mExercise.getStepCount() * 60 * 1000 / mExercise.getDuration())));
        }

        mStepChart.setOnTouchListener(null);
        XAxis xAxis = mStepChart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yAxis = mStepChart.getAxisLeft();
        yAxis.setTextColor(Color.parseColor("#999999"));
        yAxis.setGridColor(Color.parseColor("#20000000"));
        yAxis.setZeroLineColor(Color.LTGRAY);
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisMinimum(0);
        yAxis.setDrawZeroLine(true);

        mStepChart.getAxisRight().setEnabled(false);
        mStepChart.getLegend().setEnabled(false);
        mStepChart.getDescription().setEnabled(false);

        ArrayList<Entry> values = new ArrayList<Entry>();

        List<Integer> stepRecord = mExercise.getStepRecord();
        if(stepRecord == null) return;
        int i = 1;
        for (int stepCount : stepRecord) {
            values.add(new Entry(i++, stepCount));
        }

        LineDataSet set1 = new LineDataSet(values, "DataSet 1");

        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        set1.setColor(Color.parseColor("#24c789"));
        set1.setLineWidth(2f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(false);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_green);
            set1.setFillDrawable(drawable);
        }
        else {
            set1.setFillColor(Color.parseColor("#24c789"));
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mStepChart.setData(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAmapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAmapView.onResume();
        mAmapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawExercisePath();
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAmapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAmapView.onSaveInstanceState(outState);
    }

}
