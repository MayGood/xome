package com.xxhx.xome.ui.disc.trip;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.TripDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.trip.data.Trip;
import java.util.List;

public class HistoryTripActivity extends BaseActivity {

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
                .where(TripDao.Properties.Time.lt(System.currentTimeMillis()))
                .orderDesc(TripDao.Properties.Time)
                .list();

        if(trips != null && trips.size() > 0) {
            mAdapter = new TripAdapter(this, trips);
            mTripListView.setAdapter(mAdapter);
            mTripListView.setVisibility(View.VISIBLE);
        }
        else {
            mNoTripView.setVisibility(View.VISIBLE);
        }
    }

}
