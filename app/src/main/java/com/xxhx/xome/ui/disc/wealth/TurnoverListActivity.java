package com.xxhx.xome.ui.disc.wealth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.TurnoverDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.adapter.TurnoverAdapter;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TurnoverListActivity extends BaseActivity {
    public static final String EXTRA_ACCOUNT_ID = "extra_account_id";

    private static final int GROUP_TYPE = 14;

    private ListView mTurnoverList;
    private TurnoverAdapter mAdapter;
    private List<Turnover> mTurnovers;
    private Set<TurnoverType> mTypes;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_turnover_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_turnover_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTurnoverList = (ListView) findViewById(R.id.turnovers);

        long accountId = getIntent().getLongExtra(EXTRA_ACCOUNT_ID, 0);
        mTurnovers = App.getInstance().getDaoSession().getTurnoverDao().queryBuilder()
                .where(TurnoverDao.Properties.AccountId.eq(accountId))
                .orderDesc(TurnoverDao.Properties.Time)
                .list();
        mAdapter = new TurnoverAdapter(this, mTurnovers);
        mTurnoverList.setAdapter(mAdapter);
        mTurnoverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TurnoverListActivity.this, TurnoverEditActivity.class);
                intent.putExtra(TurnoverEditActivity.EXTRA_TURNOVER_ID, id);
                startActivity(intent);
            }
        });
        mTypes = new HashSet<TurnoverType>();
        for(Turnover turnover : mTurnovers) {
            mTypes.add(turnover.getTurnoverType());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_turnover_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_filter);
        if(item.hasSubMenu()) {
            SubMenu filterMenu = item.getSubMenu();
            for(TurnoverType type : mTypes) {
                filterMenu.add(GROUP_TYPE, type.ordinal(), Menu.NONE, type.getType());
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getGroupId() == GROUP_TYPE) {
            int ordinal = item.getItemId();
            List<Turnover> turnovers = new ArrayList<Turnover>();
            for(Turnover turnover : mTurnovers) {
                if(turnover.getTurnoverType().ordinal() == ordinal) turnovers.add(turnover);
            }
            mAdapter = new TurnoverAdapter(this, turnovers);
            mTurnoverList.setAdapter(mAdapter);
            return true;
        }
        else if(item.getItemId() == R.id.action_filter_all) {
            mAdapter = new TurnoverAdapter(this, mTurnovers);
            mTurnoverList.setAdapter(mAdapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
