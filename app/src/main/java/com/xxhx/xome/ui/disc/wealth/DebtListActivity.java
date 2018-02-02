package com.xxhx.xome.ui.disc.wealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.xxhx.moduleclosedatabase.debt.Debt;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Identity;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.adapter.DebtAdapter;
import java.util.Date;
import java.util.List;

public class DebtListActivity extends BaseActivity {

    private DebtAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_debt_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = findViewById(R.id.list);

        init();
    }

    private void init() {
        List<Debt> debts = App.getInstance().getEncryptedDaoSession().getDebtDao().loadAll();
        mAdapter = new DebtAdapter(this, debts);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DebtListActivity.this, NewDebtChangeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected Identity getRequiredIdentity() {
        return Identity.Manager;
    }

    public void showNewDebtDialog(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.content_new_debt_dialog, null);
        final EditText creditorEditor = contentView.findViewById(R.id.creditor);
        final EditText amountEditor = contentView.findViewById(R.id.amount);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Debt")
                .setView(contentView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String creditor = creditorEditor.getText().toString();
                        String amountStr = amountEditor.getText().toString();
                        if (!TextUtils.isEmpty(creditor) && !TextUtils.isEmpty(amountStr)) {
                            int amount = Integer.parseInt(amountStr);
                            Debt debt = new Debt();
                            debt.setCreditor(creditor);
                            debt.setAddedDate(new Date());
                            debt.setLastUpdateDate(debt.getAddedDate());
                            debt.setBaseInFens(amount * 100);
                            debt.setAmountInFens(debt.getBaseInFens());
                            App.getInstance().getEncryptedDaoSession().getDebtDao().insert(debt);
                            ToastHelper.toast("添加成功！");
                            return;
                        }
                        ToastHelper.toast("输入不正确");
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
