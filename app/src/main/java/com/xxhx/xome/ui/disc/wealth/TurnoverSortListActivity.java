package com.xxhx.xome.ui.disc.wealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.TurnoverDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.adapter.TurnoverAdapter;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TurnoverSortListActivity extends BaseActivity {
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_MONTH = "extra_month";

    private static final int GROUP_TYPE = 14;

    private ListView mTurnoverList;
    private TurnoverAdapter mAdapter;
    private List<Turnover> mTurnovers;
    private Set<TurnoverType> mTypes;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_turnover_sort_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_turnover_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTurnoverList = (ListView) findViewById(R.id.turnovers);

        String type = getIntent().getStringExtra(EXTRA_TYPE);
        mTurnovers = App.getInstance().getDaoSession().getTurnoverDao().queryBuilder()
                .where(TurnoverDao.Properties.TurnoverType.eq(TurnoverType.valueOf(type)))
                .orderDesc(TurnoverDao.Properties.Time)
                .list();
        mAdapter = new TurnoverAdapter(this, mTurnovers);
        mTurnoverList.setAdapter(mAdapter);
        mTurnoverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TurnoverSortListActivity.this, TurnoverEditActivity.class);
                intent.putExtra(TurnoverEditActivity.EXTRA_TURNOVER_ID, id);
                startActivity(intent);
            }
        });
        mTypes = new HashSet<TurnoverType>();
        for(Turnover turnover : mTurnovers) {
            mTypes.add(turnover.getTurnoverType());
        }
    }

}
