package com.xxhx.xome.ui.disc.wealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.xxhx.moduleclosedatabase.debt.Debt;
import com.xxhx.moduleclosedatabase.debt.DebtChange;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewDebtChangeActivity extends BaseActivity {

    private DebtChange mDebtChange;

    private TextView mTextDebt;
    private EditText mEditAmount;
    private EditText mEditNote;
    private CheckBox mCheckboxBase;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_debt_change);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDebtChange = new DebtChange();
        mDebtChange.setDebtId(-1l);

        mTextDebt = (TextView) findViewById(R.id.debt);
        mEditAmount = (EditText) findViewById(R.id.amount);
        mEditNote = (EditText) findViewById(R.id.note);
        mCheckboxBase = findViewById(R.id.checkbox_base);
    }

    public void confirm(View view) {
        long change = 0;
        try {
            BigDecimal bd = new BigDecimal(mEditAmount.getText().toString());
            change = bd.multiply(new BigDecimal(100)).longValue();
        } catch (NumberFormatException e) {
            change = 0;
        }
        Debt debt = App.getInstance().getEncryptedDaoSession().getDebtDao().load(mDebtChange.getDebtId());
        if(debt == null) {
            ToastHelper.toast("请选择所属债务");
            return;
        }
        if(change == 0) {
            ToastHelper.toast("请输入正确的金额");
            return;
        }
        if(TextUtils.isEmpty(mEditNote.getText().toString())) {
            ToastHelper.toast("请输入备注信息");
            return;
        }
        mDebtChange.setDate(new Date(System.currentTimeMillis()));
        mDebtChange.setNotes(mEditNote.getText().toString());
        mDebtChange.setChangeInFens(change);

        if (mCheckboxBase.isChecked()) {
            debt.setBaseInFens(debt.getBaseInFens() - change);
        }
        debt.setAmountInFens(debt.getAmountInFens() - change);
        debt.setLastUpdateDate(mDebtChange.getDate());
        App.getInstance().getEncryptedDaoSession().getDebtDao().update(debt);
        App.getInstance().getEncryptedDaoSession().getDebtChangeDao().insert(mDebtChange);
        finish();
    }

    public void chooseDebt(View view) {
        List<Debt> debtList = App.getInstance().getEncryptedDaoSession().getDebtDao().loadAll();
        final List<Long> debtIdList = new ArrayList<Long>();
        final List<String> debtNames = new ArrayList<String>();
        for(Debt debt : debtList) {
            debtIdList.add(debt.getId());
            debtNames.add(debt.getCreditor());
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(debtNames.toArray(new String[debtNames.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextDebt.setText(debtNames.get(which));
                        mDebtChange.setDebtId(debtIdList.get(which));
                    }
                })
                .create();
        dialog.show();
    }

}
