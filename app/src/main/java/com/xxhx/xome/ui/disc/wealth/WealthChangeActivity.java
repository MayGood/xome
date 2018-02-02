package com.xxhx.xome.ui.disc.wealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.WealthAccountDao;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WealthChangeActivity extends BaseActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_WEALTH_OUT = "extra_wealth_out";

    private final DateFormat fGainDateFormat = new SimpleDateFormat("M月d日收益", Locale.CHINA);

    private boolean mWealthOut;
    private WealthAccount mWealthAccount;
    private WealthAccount mRelatedWealthAccount;
    private Turnover mTurnover;

    private TextView mTextType;
    private View mGroupAccount;
    private TextView mTextAccount;
    private EditText mEditAmount;
    private EditText mEditNote;

    private Animation mAnimItemIn;
    private Animation mAnimItemOut;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wealth_change);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWealthOut = getIntent().getBooleanExtra(EXTRA_WEALTH_OUT, false);
        if(mWealthOut) {
            getSupportActionBar().setTitle(R.string.wealth_out);
        }
        else {
            getSupportActionBar().setTitle(R.string.wealth_in);
        }

        mWealthAccount = App.getInstance().getDaoSession().getWealthAccountDao().load(getIntent().getLongExtra(EXTRA_ID, 0));
        mTurnover = new Turnover();

        mTextType = (TextView) findViewById(R.id.type);
        mGroupAccount = findViewById(R.id.item_account);
        mTextAccount = (TextView) findViewById(R.id.account);
        mEditAmount = (EditText) findViewById(R.id.amount);
        mEditNote = (EditText) findViewById(R.id.note);
    }

    public void confirm(View view) {
        long change = 0;
        try {
            BigDecimal bd = new BigDecimal(mEditAmount.getText().toString());
            change = bd.multiply(new BigDecimal(100)).longValue();
        } catch (NumberFormatException e) {
            change = 0;
        }
        if(mTurnover.getTurnoverType() == null) {
            ToastHelper.toast("请选择交易类别");
            return;
        }
        if(mTurnover.getTurnoverType() == TurnoverType.TRANSFER && TextUtils.isEmpty(mTextAccount.getText().toString())) {
            ToastHelper.toast("请选择关联账户");
            return;
        }
        if(change <= 0) {
            ToastHelper.toast("请输入正确的交易金额");
            return;
        }
        if(TextUtils.isEmpty(mEditNote.getText().toString())) {
            ToastHelper.toast("请输入交易备注信息");
            return;
        }
        if(change > 0) {
            mTurnover.setAccountId(mWealthAccount.getId());
            mTurnover.setTime(new Date(System.currentTimeMillis()));
            mTurnover.setNote(mEditNote.getText().toString());
            long balanceInFens = mWealthAccount.getBalanceInFens();
            if(mWealthOut) {
                mTurnover.setAmountInFens(-change);
                mWealthAccount.setBalanceInFens(balanceInFens - change);
            }
            else {
                mTurnover.setAmountInFens(change);
                mWealthAccount.setBalanceInFens(balanceInFens + change);
            }
            mWealthAccount.setUpdatedTime(new Date(System.currentTimeMillis()));
            App.getInstance().getDaoSession().getWealthAccountDao().update(mWealthAccount);
            App.getInstance().getDaoSession().getTurnoverDao().insert(mTurnover);
            if(mTurnover.getTurnoverType() == TurnoverType.TRANSFER && mRelatedWealthAccount != null) {
                Turnover turnover = new Turnover();
                turnover.setTurnoverType(TurnoverType.TRANSFER);
                turnover.setAccountId(mRelatedWealthAccount.getId());
                turnover.setTime(mTurnover.getTime());
                turnover.setNote(mTurnover.getNote());
                long relatedBalanceInFens = mRelatedWealthAccount.getBalanceInFens();
                if(mWealthOut) {
                    turnover.setAmountInFens(change);
                    mRelatedWealthAccount.setBalanceInFens(relatedBalanceInFens + change);
                }
                else {
                    turnover.setAmountInFens(-change);
                    mRelatedWealthAccount.setBalanceInFens(relatedBalanceInFens - change);
                }
                mRelatedWealthAccount.setUpdatedTime(new Date(System.currentTimeMillis()));
                App.getInstance().getDaoSession().getWealthAccountDao().update(mRelatedWealthAccount);
                App.getInstance().getDaoSession().getTurnoverDao().insert(turnover);
            }
            finish();
        }
    }

    private void showGroupAccount() {
        if(mGroupAccount.getVisibility() != View.VISIBLE) {
            mGroupAccount.setVisibility(View.VISIBLE);
            if(mAnimItemIn == null) {
                mAnimItemIn = AnimationUtils.loadAnimation(this, R.anim.item_fade_in);
            }
            mGroupAccount.startAnimation(mAnimItemIn);
        }
    }

    private void hideGroupAccount() {
        if(mGroupAccount.getVisibility() == View.VISIBLE) {
            if(mAnimItemOut == null) {
                mAnimItemOut = AnimationUtils.loadAnimation(this, R.anim.item_fade_out);
                mAnimItemOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mGroupAccount.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            mGroupAccount.startAnimation(mAnimItemOut);
        }
    }

    public void chooseType(View view) {
        //final TurnoverType[] turnoverTypes = TurnoverType.values();
        //final String[] typeNames = new String[turnoverTypes.length];
        final List<TurnoverType> turnoverTypes = new ArrayList<TurnoverType>();
        final List<String> typeNames = new ArrayList<String>();
        if(mWealthOut) {
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
                        if(mTurnover.getTurnoverType() == TurnoverType.TRANSFER) {
                            showGroupAccount();
                        }
                        else {
                            if(mTurnover.getTurnoverType() == TurnoverType.GAIN && TextUtils.isEmpty(mEditNote.getText().toString())) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DAY_OF_MONTH, -1);
                                mEditNote.setText(fGainDateFormat.format(calendar.getTime()));
                            }
                            hideGroupAccount();
                        }
                    }
                })
                .create();
        dialog.show();
    }

    public void chooseAccount(View view) {
        List<WealthAccount> myAccounts =  App.getInstance().getDaoSession().getWealthAccountDao().queryBuilder()
                .orderDesc(WealthAccountDao.Properties.UpdatedTime).list();
        final List<WealthAccount> accounts = new ArrayList<WealthAccount>();
        final List<String> accountNames = new ArrayList<String>();
        accountNames.add("无");
        long accountId = mWealthAccount.getId();
        for(WealthAccount wealthAccount : myAccounts) {
            if(wealthAccount.getId() != accountId) {
                accounts.add(wealthAccount);
                accountNames.add(wealthAccount.getDisplayName());
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(accountNames.toArray(new String[accountNames.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextAccount.setText(accountNames.get(which));
                        if(which > 0) {
                            mRelatedWealthAccount = accounts.get(which - 1);
                        }
                        else {
                            mRelatedWealthAccount = null;
                        }
                    }
                })
                .create();
        dialog.show();
    }
}
