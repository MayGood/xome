package com.xxhx.xome.ui.disc.wealth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.CreditBillDao;
import com.xxhx.xome.data.CreditCardDao;
import com.xxhx.xome.data.TurnoverDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.data.AccountType;
import com.xxhx.xome.ui.disc.wealth.data.CreditBill;
import com.xxhx.xome.ui.disc.wealth.data.CreditCard;
import com.xxhx.xome.ui.disc.wealth.data.Currency;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import com.xxhx.xome.ui.disc.wealth.data.TurnoverType;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import com.xxhx.xome.ui.disc.wealth.util.MyAxisValueFormatter;
import com.xxhx.xome.ui.disc.wealth.util.MyValueFormatter;
import com.xxhx.xome.util.CommonUtil;
import com.xxhx.xome.util.StringUtil;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WealthAccountDetailActivity extends BaseActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/M/d");

    private WealthAccount mWealthAccount;
    private CreditCard mCreditCard;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private CreditBill mCreditBill;

    private TextView mTextAddedTime;
    private TextView mTextWarning;
    private TextView mTextOrg;
    private TextView mTextType;
    private TextView mTextNo;
    private TextView mTextBalance;
    private TextView mTextBill;
    private View mGroupGainChart;
    private LineChart mLineChart;
    private View mGroupRecentTurnover;
    private ViewGroup mListTurnovers;

    private View mGroupCreditInfo;
    private TextView mTextBillAndRepaymentDay;
    private TextView mTextExpireDate;

    private View mGroupWealthChange;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wealth_account_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextAddedTime = (TextView) findViewById(R.id.item_added_time);
        mTextWarning = (TextView) findViewById(R.id.item_warning);
        mTextOrg = (TextView) findViewById(R.id.item_org);
        mTextType = (TextView) findViewById(R.id.item_type);
        mTextNo = (TextView) findViewById(R.id.item_no);
        mTextBalance = (TextView) findViewById(R.id.item_balance);
        mTextBill = (TextView) findViewById(R.id.item_bill);
        mGroupCreditInfo = findViewById(R.id.section_credit_info);
        mTextBillAndRepaymentDay = (TextView) findViewById(R.id.item_bill_repayment_day);
        mTextExpireDate = (TextView) findViewById(R.id.item_expire_date);
        mTextNo.setTypeface(App.getInstance().getRobotoTypeface());
        mTextBalance.setTypeface(App.getInstance().getRobotoTypeface());
        mTextBill.setTypeface(App.getInstance().getRobotoTypeface());

        mGroupWealthChange = findViewById(R.id.section_wealth_change);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wealth_account_detail, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        WealthAccount wealthAccount = App.getInstance().getDaoSession().getWealthAccountDao().load(getIntent().getLongExtra(EXTRA_ID, 0));
        initAccount(wealthAccount);

        mGroupWealthChange.setVisibility(View.GONE);
        mGroupWealthChange.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGroupWealthChange.setVisibility(View.VISIBLE);
                mGroupWealthChange.startAnimation(AnimationUtils.loadAnimation(WealthAccountDetailActivity.this, R.anim.slide_in_bottom));
            }
        }, 400);
    }

    private void initAccount(WealthAccount wealthAccount) {
        if(wealthAccount == null) {
            finish();
            return;
        }
        mWealthAccount = wealthAccount;
        getSupportActionBar().setTitle(mWealthAccount.getDisplayName());
        long relativeDays = CommonUtil.getRelativeDays(mWealthAccount.getAddedTime().getTime());
        if(relativeDays == 0) {
            mTextAddedTime.setText(getString(R.string.added_time_in_date, getString(R.string.today)));
        }
        else if(relativeDays > 0 && relativeDays <= 14) {
            mTextAddedTime.setText(getString(R.string.added_time_in_days, relativeDays));
        }
        else {
            mTextAddedTime.setText(getString(R.string.added_time_in_date, sDateFormat.format(mWealthAccount.getAddedTime())));
        }
        if(mWealthAccount.getBalanceInFens() < 0) {
            mTextBalance.setText(mWealthAccount.getFormattedBalance());
            mTextBalance.setTextColor(getResources().getColor(R.color.negativeWealth));
        }
        else {
            mTextBalance.setText(mWealthAccount.getFormattedBalance());
            mTextBalance.setTextColor(getResources().getColor(R.color.positiveWealth));
        }
        mTextOrg.setText(mWealthAccount.getOrganization().getName());
        if(mWealthAccount.getType() == AccountType.CREDIT) {
            mCreditCard = App.getInstance().getDaoSession().getCreditCardDao().queryBuilder().where(
                    CreditCardDao.Properties.No.eq(mWealthAccount.getNo())).list().get(0);
            mTextExpireDate.setText(mCreditCard.getExpireDate().substring(4) + "/" + mCreditCard.getExpireDate().substring(0, 4));
            mTextType.setText(R.string.credit);
            mTextNo.setText(StringUtil.getFormattedBankCardNo(mWealthAccount.getNo()));
            Calendar calendarNow = Calendar.getInstance();
            mYear = calendarNow.get(Calendar.YEAR);
            mMonth = calendarNow.get(Calendar.MONTH);
            mDayOfMonth = calendarNow.get(Calendar.DAY_OF_MONTH);

            if(mDayOfMonth >= mCreditCard.getBillDay()) {
                List<CreditBill> creditBills = App.getInstance().getDaoSession().getCreditBillDao()
                        .queryBuilder()
                        .where(CreditBillDao.Properties.CreditNo.eq(mWealthAccount.getNo()),
                                CreditBillDao.Properties.Year.eq(mYear),
                                CreditBillDao.Properties.Month.eq(mMonth))
                        .list();
                if(creditBills != null && creditBills.size() > 0) {
                    mCreditBill = creditBills.get(0);
                }
            }
            else if(mMonth > 0) {
                List<CreditBill> creditBills = App.getInstance().getDaoSession().getCreditBillDao()
                        .queryBuilder()
                        .where(CreditBillDao.Properties.CreditNo.eq(mWealthAccount.getNo()),
                                CreditBillDao.Properties.Year.eq(mYear),
                                CreditBillDao.Properties.Month.eq(mMonth - 1))
                        .list();
                if(creditBills != null && creditBills.size() > 0) {
                    mCreditBill = creditBills.get(0);
                }
            }
            else {
                List<CreditBill> creditBills = App.getInstance().getDaoSession().getCreditBillDao()
                        .queryBuilder()
                        .where(CreditBillDao.Properties.CreditNo.eq(mWealthAccount.getNo()),
                                CreditBillDao.Properties.Year.eq(mYear - 1),
                                CreditBillDao.Properties.Month.eq(Calendar.DECEMBER))
                        .list();
                if(creditBills != null && creditBills.size() > 0) {
                    mCreditBill = creditBills.get(0);
                }
            }
            if(mCreditCard.getBillDay() < mCreditCard.getRepaymentDay()) {
                mTextBillAndRepaymentDay.setText(getString(R.string.bill_repayment_day_1, mCreditCard.getBillDay(), mCreditCard.getRepaymentDay()));
                if(mDayOfMonth >= mCreditCard.getBillDay() && mDayOfMonth <= mCreditCard.getRepaymentDay()) {
                    if(mCreditBill == null) {
                        mTextBill.setText("未出账单");
                    }
                    else if(mCreditBill.getPaidOff()) {
                        mTextBill.setText("已还清");
                    }
                    else {
                        mTextBill.setText("待还 " + mCreditBill.getFormattedBill());
                    }
                }
                else {
                    mTextBill.setText("未出账单");
                }
            }
            else {
                mTextBillAndRepaymentDay.setText(getString(R.string.bill_repayment_day_2, mCreditCard.getBillDay(), mCreditCard.getRepaymentDay()));
                if(mDayOfMonth >= mCreditCard.getBillDay() || mDayOfMonth <= mCreditCard.getRepaymentDay()) {
                    if(mCreditBill == null) {
                        mTextBill.setText("未出账单");
                    }
                    else if(mCreditBill.getPaidOff()) {
                        mTextBill.setText("已还清");
                    }
                    else {
                        mTextBill.setText("待还 " + mCreditBill.getFormattedBill());
                    }
                }
                else {
                    mTextBill.setText("未出账单");
                }
            }

            List<CreditBill> unPaidBills = App.getInstance().getDaoSession().getCreditBillDao().queryBuilder().where(
                    CreditBillDao.Properties.CreditNo.eq(wealthAccount.getNo()), CreditBillDao.Properties.PaidOff.eq(false))
                    .list();
            long dayToDeadline = Long.MAX_VALUE;
            if(unPaidBills != null && unPaidBills.size() > 0) {
                String deadline = unPaidBills.get(0).getDeadline();
                if(deadline != null && deadline.length() == 8) {
                    try {
                        int year = Integer.parseInt(deadline.substring(0, 4));
                        int month = Integer.parseInt(deadline.substring(4, 6));
                        int dayOfMonth = Integer.parseInt(deadline.substring(6));
                        Calendar billCalendar = Calendar.getInstance();
                        billCalendar.set(year, month, dayOfMonth);
                        dayToDeadline = -CommonUtil.getRelativeDays(billCalendar.getTime().getTime());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(dayToDeadline > 0 && dayToDeadline != Long.MAX_VALUE) {
                mTextWarning.setText(getString(R.string.days_to_deadline, dayToDeadline));
                mTextWarning.setVisibility(View.VISIBLE);
            }
            else {
                mTextWarning.setVisibility(View.GONE);
            }
            mTextNo.setVisibility(View.VISIBLE);
            mTextBill.setVisibility(View.VISIBLE);
            mGroupCreditInfo.setVisibility(View.VISIBLE);
        }
        else if(mWealthAccount.getType() == AccountType.DEBIT) {
            mTextType.setText(R.string.debit);
            mTextNo.setText(StringUtil.getFormattedBankCardNo(mWealthAccount.getNo()));
            mTextNo.setVisibility(View.VISIBLE);
            mTextWarning.setVisibility(View.GONE);
            mTextBill.setVisibility(View.GONE);
            mGroupCreditInfo.setVisibility(View.GONE);
        }
        else if(mWealthAccount.getType() == AccountType.INTERNET) {
            mTextType.setText(R.string.internet_finance);
            mTextWarning.setVisibility(View.GONE);
            mTextNo.setVisibility(View.GONE);
            mTextBill.setVisibility(View.GONE);
            mGroupCreditInfo.setVisibility(View.GONE);

            if(mGroupGainChart == null) {
                mGroupGainChart = ((ViewStub) findViewById(R.id.stub_gain_chart)).inflate();
                mLineChart = (LineChart) mGroupGainChart.findViewById(R.id.chart);
            }

            mGroupGainChart.setVisibility(View.VISIBLE);
            mLineChart.getLegend().setEnabled(false);
            mLineChart.getDescription().setEnabled(false);

            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            mLineChart.getAxisRight().setEnabled(false);

            List<Turnover> gains = App.getInstance().getDaoSession().getTurnoverDao().queryBuilder()
                    .where(TurnoverDao.Properties.AccountId.eq(mWealthAccount.getId())
                            , TurnoverDao.Properties.TurnoverType.eq(TurnoverType.GAIN))
                    .orderDesc(TurnoverDao.Properties.Time)
                    .limit(7)
                    .list();
            setData(gains);
        }
        else {
            mTextType.setText(null);
            //mTextNo.setText(mWealthAccount.getNo());
            mTextNo.setVisibility(View.VISIBLE);
            mTextWarning.setVisibility(View.GONE);
            mTextBill.setVisibility(View.GONE);
            mGroupCreditInfo.setVisibility(View.GONE);
        }

        List<Turnover> turnovers = App.getInstance().getDaoSession().getTurnoverDao().queryBuilder()
                .where(TurnoverDao.Properties.AccountId.eq(mWealthAccount.getId()))
                .orderDesc(TurnoverDao.Properties.Time)
                .limit(5)
                .list();
        if(turnovers != null && turnovers.size() > 0) {
            if(mGroupRecentTurnover == null) {
                mGroupRecentTurnover = ((ViewStub) findViewById(R.id.stub_recent_turnover)).inflate();
                mListTurnovers = (ViewGroup) mGroupRecentTurnover.findViewById(R.id.turnovers);
            }
            initTurnoverList(turnovers);
            mGroupRecentTurnover.setVisibility(View.VISIBLE);
        }
        else {
            if(mGroupRecentTurnover != null) {
                mGroupRecentTurnover.setVisibility(View.GONE);
            }
        }
    }

    private void setData(List<Turnover> gains) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        int j = 1;
        for (int i = gains.size() - 1; i >= 0; i--) {
            Turnover gain = gains.get(i);
            float val = (float) gain.getAmountInFens() / 100;
            values.add(new Entry(j++, val));
        }

        LineDataSet set1 = new LineDataSet(values, "DataSet 1");

        set1.setDrawIcons(false);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(getResources().getColor(R.color.darkDisc));
        set1.setCircleColor(getResources().getColor(R.color.darkDisc));
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);
        set1.setValueFormatter(new MyValueFormatter());

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
            //set1.setFillDrawable(drawable);
            set1.setFillColor(getResources().getColor(R.color.disc));
        }
        else {
            set1.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    private void initTurnoverList(List<Turnover> turnovers) {
        if(mListTurnovers == null)
            return;
        mListTurnovers.removeAllViews();
        for(Turnover turnover : turnovers) {
            mListTurnovers.addView(getTurnoverView(turnover));
        }
    }

    private View getTurnoverView(final Turnover turnover) {
        View view = LayoutInflater.from(this).inflate(R.layout.list_item_turnover, null);
        TextView hour = (TextView) view.findViewById(R.id.hour);
        TextView minute = (TextView) view.findViewById(R.id.minute);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView amount = (TextView) view.findViewById(R.id.amount);
        TextView type = (TextView) view.findViewById(R.id.type);
        hour.setTypeface(App.getInstance().getRobotoTypeface());
        minute.setTypeface(App.getInstance().getRobotoTypeface());
        date.setTypeface(App.getInstance().getRobotoTypeface());
        amount.setTypeface(App.getInstance().getRobotoTypeface());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(turnover.getTime());
        hour.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        minute.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
        date.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
        description.setText(turnover.getNote());
        type.setText(turnover.getTurnoverType().getType());
        if(turnover.getAmountInFens() >= 0) {
            amount.setTextColor(getResources().getColor(R.color.positiveWealth));
        }
        else {
            amount.setTextColor(getResources().getColor(R.color.negativeWealth));
        }
        amount.setText(turnover.getFormattedAmount());
        //view.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        long balanceInFens = mWealthAccount.getBalanceInFens();
        //        mWealthAccount.setBalanceInFens(balanceInFens - 923);
        //        App.getInstance().getDaoSession().getWealthAccountDao().update(mWealthAccount);
        //        //turnover.setTurnoverType(TurnoverType.TRANSFER);
        //        //App.getInstance().getDaoSession().getTurnoverDao().update(turnover);
        //        //App.getInstance().getDaoSession().getTurnoverDao().delete(turnover);
        //    }
        //});
        return view;
    }

    public void wealthOut(View view) {
        Intent intent = new Intent(this, WealthChangeActivity.class);
        intent.putExtra(WealthChangeActivity.EXTRA_ID, mWealthAccount.getId());
        intent.putExtra(WealthChangeActivity.EXTRA_WEALTH_OUT, true);
        startActivity(intent);
    }

    public void wealthIn(View view) {
        Intent intent = new Intent(this, WealthChangeActivity.class);
        intent.putExtra(WealthChangeActivity.EXTRA_ID, mWealthAccount.getId());
        startActivity(intent);
    }

    public void showAllTurnovers(View view) {
        Intent intent = new Intent(this, TurnoverListActivity.class);
        intent.putExtra(TurnoverListActivity.EXTRA_ACCOUNT_ID, mWealthAccount.getId());
        startActivity(intent);
    }

    private void hideSoftInput(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void editBill(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_bill_edit, null);
        final EditText editBill = (EditText) contentView.findViewById(R.id.bill);
        if(mCreditBill == null) {
            contentView.findViewById(R.id.option_payoff).setVisibility(View.GONE);
        }
        else {
            contentView.findViewById(R.id.option_bill_not_ready).setVisibility(View.GONE);
        }
        final RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.radio_group_bill);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.option_payoff:
                        hideSoftInput(editBill);
                        editBill.setVisibility(View.GONE);
                        break;
                    case R.id.option_bill_not_ready:
                        hideSoftInput(editBill);
                        editBill.setVisibility(View.GONE);
                        break;
                    case R.id.option_bill_ready:
                        editBill.setVisibility(View.VISIBLE);
                        if(mCreditBill != null) {
                            editBill.setText(String.format("%d.%02d", mCreditBill.getBillInFens() / 100, Math.abs(mCreditBill.getBillInFens()) % 100));
                        }
                        editBill.requestFocus();
                        break;
                }
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(this).setView(contentView)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(radioGroup.getCheckedRadioButtonId() == R.id.option_bill_ready) {
                            BigDecimal bd = new BigDecimal(editBill.getText().toString());
                            long billInFens = bd.multiply(new BigDecimal(100)).longValue();
                            if(mCreditBill == null) {
                                CreditBill creditBill = new CreditBill();
                                creditBill.setCreditNo(mWealthAccount.getNo());
                                creditBill.setBillInFens(billInFens);
                                creditBill.setCurrency(Currency.RMB);
                                creditBill.setPaidOff(false);
                                if(mDayOfMonth >= mCreditCard.getBillDay()) {
                                    creditBill.setYear(mYear);
                                    creditBill.setMonth(mMonth);
                                }
                                else if(mMonth > 0) {
                                    creditBill.setYear(mYear);
                                    creditBill.setMonth(mMonth - 1);
                                }
                                else {
                                    creditBill.setYear(mYear - 1);
                                    creditBill.setMonth(mMonth);
                                }
                                if(mCreditCard.getBillDay() < mCreditCard.getRepaymentDay()) {
                                    creditBill.setDeadline(String.format("%04d%02d%02d", mYear, mMonth, mCreditCard.getRepaymentDay()));
                                }
                                else {
                                    creditBill.setDeadline(String.format("%04d%02d%02d", mYear, mMonth + 1, mCreditCard.getRepaymentDay()));
                                }
                                App.getInstance().getDaoSession().getCreditBillDao().insert(creditBill);
                            }
                            else {
                                mCreditBill.setPaidOff(false);
                                mCreditBill.setBillInFens(billInFens);
                                if(mCreditCard.getBillDay() < mCreditCard.getRepaymentDay()) {
                                    mCreditBill.setDeadline(String.format("%04d%02d%02d", mYear, mMonth, mCreditCard.getRepaymentDay()));
                                }
                                else {
                                    mCreditBill.setDeadline(String.format("%04d%02d%02d", mYear, mMonth + 1, mCreditCard.getRepaymentDay()));
                                }
                                App.getInstance().getDaoSession().getCreditBillDao().update(mCreditBill);
                            }
                        }
                        else if(radioGroup.getCheckedRadioButtonId() == R.id.option_payoff) {
                            mCreditBill.setPaidOff(true);
                            App.getInstance().getDaoSession().getCreditBillDao().update(mCreditBill);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }
}
