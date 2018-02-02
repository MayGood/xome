package com.xxhx.xome.ui.disc.wealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TurnoverEditActivity extends BaseActivity {
    public static final String EXTRA_TURNOVER_ID = "extra_turnover_id";

    private Turnover mTurnover;

    private TextView mTextType;
    private EditText mEditAmount;
    private EditText mEditNote;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_turnover_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTurnover = App.getInstance().getDaoSession().getTurnoverDao().load(getIntent().getLongExtra(EXTRA_TURNOVER_ID, -1));
        if(mTurnover == null) {
            finish();
            return;
        }
        if(mTurnover.getAmountInFens() >= 0) {
            getSupportActionBar().setSubtitle(R.string.wealth_in);
        }
        else {
            getSupportActionBar().setSubtitle(R.string.wealth_out);
        }
        mTextType = (TextView) findViewById(R.id.type);
        mEditAmount = (EditText) findViewById(R.id.amount);
        mEditNote = (EditText) findViewById(R.id.note);
        initView();
    }

    private void initView() {
        mTextType.setText(mTurnover.getTurnoverType().getType());
        mEditAmount.setText(String.format("%.2f", Math.abs(mTurnover.getAmountInFens()) / 100.0));
        mEditAmount.setSelection(mEditAmount.getText().length());
        mEditNote.setText(mTurnover.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_turnover_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                long newAmount = 0;
                long change = 0;
                try {
                    BigDecimal bd = new BigDecimal(mEditAmount.getText().toString());
                    newAmount = bd.multiply(new BigDecimal(100)).longValue();
                } catch (NumberFormatException e) {
                    newAmount = 0;
                }

                if(newAmount == 0) {
                    ToastHelper.toast("修改的金额不能为零");
                    return true;
                }

                if(mTurnover.getAmountInFens() >= 0) {
                    change = newAmount - mTurnover.getAmountInFens();
                    mTurnover.setAmountInFens(newAmount);
                }
                else {
                    change = -newAmount - mTurnover.getAmountInFens();
                    mTurnover.setAmountInFens(-newAmount);
                }
                String note = mEditNote.getText().toString();
                if(!TextUtils.isEmpty(note)) {
                    mTurnover.setNote(note);
                }
                WealthAccount wealthAccount = App.getInstance().getDaoSession().getWealthAccountDao().load(mTurnover.getAccountId());
                wealthAccount.setBalanceInFens(wealthAccount.getBalanceInFens() + change);
                App.getInstance().getDaoSession().getWealthAccountDao().update(wealthAccount);
                App.getInstance().getDaoSession().getTurnoverDao().update(mTurnover);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseType(View view) {
        //final TurnoverType[] turnoverTypes = TurnoverType.values();
        //final String[] typeNames = new String[turnoverTypes.length];
        final List<TurnoverType> turnoverTypes = new ArrayList<TurnoverType>();
        final List<String> typeNames = new ArrayList<String>();
        if(mTurnover.getAmountInFens() < 0) {
            for(TurnoverType turnoverType : TurnoverType.values()) {
                if(turnoverType.isWealthOut()) {
                    turnoverTypes.add(turnoverType);
                    typeNames.add(turnoverType.getType());
                }
            }
        }
        else {
            for(TurnoverType turnoverType : TurnoverType.values()) {
                if(turnoverType.isWealthIn()) {
                    turnoverTypes.add(turnoverType);
                    typeNames.add(turnoverType.getType());
                }
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(typeNames.toArray(new String[typeNames.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextType.setText(typeNames.get(which));
                        mTurnover.setTurnoverType(turnoverTypes.get(which));
                    }
                })
                .create();
        dialog.show();
    }
}
