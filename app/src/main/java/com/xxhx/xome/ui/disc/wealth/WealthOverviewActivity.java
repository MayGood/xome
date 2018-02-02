package com.xxhx.xome.ui.disc.wealth;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Identity;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.adapter.OverviewListAdapter;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import java.util.ArrayList;
import java.util.List;

public class WealthOverviewActivity extends BaseActivity {

    private OverviewListAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wealth_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.list);

        List<TurnoverType> types = new ArrayList<TurnoverType>();
        types.add(TurnoverType.TRANSFER);
        types.add(TurnoverType.CONSUMPTION);
        types.add(TurnoverType.WAGE);
        types.add(TurnoverType.GAIN);
        mAdapter = new OverviewListAdapter(this, types);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected Identity getRequiredIdentity() {
        return Identity.Manager;
    }
}
