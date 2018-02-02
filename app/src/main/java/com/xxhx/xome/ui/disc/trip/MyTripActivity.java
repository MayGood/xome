package com.xxhx.xome.ui.disc.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.google.gson.Gson;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.TripDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.trip.data.TimeTable;
import com.xxhx.xome.ui.disc.trip.data.Trip;
import com.xxhx.xome.ui.disc.trip.data.TripType;
import com.xxhx.xome.util.StringUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyTripActivity extends BaseActivity {
    private static final long sOneDayTimeInMillis = 24 * 3600 *1000;

    private View mNoTripView;
    private ListView mTripListView;
    private TripAdapter mAdapter;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNoTripView = findViewById(R.id.no_trip);
        mTripListView = (ListView) findViewById(R.id.trips);
        List<Trip> trips = App.getInstance().getDaoSession().getTripDao().queryBuilder()
                .where(TripDao.Properties.Time.ge(System.currentTimeMillis() - sOneDayTimeInMillis))
                .orderAsc(TripDao.Properties.Time)
                .list();
        if(trips != null && trips.size() > 0) {
            mAdapter = new TripAdapter(this, trips);
            mTripListView.setAdapter(mAdapter);
            mTripListView.setVisibility(View.VISIBLE);
        }
        else {
            mNoTripView.setVisibility(View.VISIBLE);
        }

        try {
            String d = StringUtil.inputStreamToString(getAssets().open("trips/g660.json"));
            if(d != null) {
                Gson gson = new Gson();
                TimeTable tt = gson.fromJson(d, TimeTable.class);
                d.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_flight:
                Intent addFlightIntent = new Intent(this, NewTripActivity.class);
                addFlightIntent.putExtra(NewTripActivity.EXTRA_TYPE, NewTripActivity.TYPE_FLIGHT);
                startActivity(addFlightIntent);
                return true;
            case R.id.action_add_railway:
                Intent addRailwayIntent = new Intent(this, NewTripActivity.class);
                startActivity(addRailwayIntent);
                return true;
            case R.id.action_history:
                Intent historyIntent = new Intent(this, HistoryTripActivity.class);
                startActivity(historyIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
