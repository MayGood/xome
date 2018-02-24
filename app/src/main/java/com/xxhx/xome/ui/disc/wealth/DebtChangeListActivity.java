package com.xxhx.xome.ui.disc.wealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.xxhx.moduleclosedatabase.debt.Debt;
import com.xxhx.moduleclosedatabase.debt.DebtChange;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Identity;
import com.xxhx.xome.data.close.DebtChangeDao;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.adapter.DebtAdapter;
import com.xxhx.xome.ui.disc.wealth.adapter.DebtChangeAdapter;
import java.util.Date;
import java.util.List;

public class DebtChangeListActivity extends BaseActivity {
    public static final String EXTRA_DEBT_ID = "extra_debt_id";

    private DebtChangeAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_debt_change_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = findViewById(R.id.list);

        long debtId = getIntent().getLongExtra(EXTRA_DEBT_ID, -1);
        if (debtId < 0) {
            finish();
            return;
        }
        init(debtId);
    }

    private void init(long debtId) {
        List<DebtChange> debtChanges = App.getInstance().getEncryptedDaoSession().getDebtChangeDao()
                .queryBuilder().where(DebtChangeDao.Properties.DebtId.eq(debtId))
                .list();
        mAdapter = new DebtChangeAdapter(this, debtChanges);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected Identity getRequiredIdentity() {
        return Identity.Manager;
    }
}
