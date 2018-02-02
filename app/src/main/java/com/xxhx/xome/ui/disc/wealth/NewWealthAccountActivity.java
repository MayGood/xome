package com.xxhx.xome.ui.disc.wealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.data.AccountType;
import com.xxhx.xome.ui.disc.wealth.data.CreditCard;
import com.xxhx.xome.ui.disc.wealth.data.Organization;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import java.math.BigDecimal;
import java.util.Date;

public class NewWealthAccountActivity extends BaseActivity {

    private TextView mTextOrg;
    private EditText mEditNo;
    private EditText mEditBalance;
    private EditText mEditAlias;

    private CheckBox mCheckCredit;
    private View mGroupCredit;
    private EditText mEditBillDay;
    private EditText mEditRepaymentDay;
    private EditText mEditExpireDate;

    private WealthAccount mWealthAccount;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_wealth_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.new_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextOrg = (TextView) findViewById(R.id.item_org);
        mEditNo = (EditText) findViewById(R.id.item_no);
        mEditBalance = (EditText) findViewById(R.id.item_balance);
        mEditAlias = (EditText) findViewById(R.id.item_alias);
        mCheckCredit = (CheckBox) findViewById(R.id.check_credit);
        mGroupCredit = findViewById(R.id.section_credit);
        mEditBillDay = (EditText) findViewById(R.id.item_bill_day);
        mEditRepaymentDay = (EditText) findViewById(R.id.item_repayment_day);
        mEditExpireDate = (EditText) findViewById(R.id.item_expire_date);
        //mEditNo.addTextChangedListener(mBankIdNumWatcher);
        mCheckCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mGroupCredit.setVisibility(View.VISIBLE);
                }
                else {
                    mGroupCredit.setVisibility(View.GONE);
                }
            }
        });
        mWealthAccount = new WealthAccount();

    }

    private TextWatcher mBankIdNumWatcher = new TextWatcher() {
        private int beforeLength = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        private void updateText(String text) {
            mEditNo.removeTextChangedListener(this);
            mEditNo.setText(text);
            mEditNo.setSelection(text.length());
            mEditNo.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            if(length > beforeLength && (length == 4 || length == 9 || length == 14)) {
                s.append(" ");
                updateText(s.toString());
            }
            beforeLength = s.length();
        }
    };

    public void chooseOrganization(View view) {
        final Organization[] organizations = Organization.values();
        final String[] orgNames = new String[organizations.length];
        for(int i = 0; i < organizations.length; i++) {
            orgNames[i] = organizations[i].getName();
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(orgNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextOrg.setText(orgNames[which]);
                        mWealthAccount.setOrganization(organizations[which]);
                    }
                })
                .create();
        dialog.show();
    }

    public void confirm(View view) {
        if(mWealthAccount.getOrganization() == null) {
            ToastHelper.toast("请选择卡/账户开立公司");
            return;
        }
        if(TextUtils.isEmpty(mEditNo.getText().toString())) {
            ToastHelper.toast("请输入卡号");
            return;
        }
        mWealthAccount.setAddedTime(new Date(System.currentTimeMillis()));
        try {
            BigDecimal bd = new BigDecimal(mEditBalance.getText().toString());
            long balanceInFens = bd.multiply(new BigDecimal(100)).longValue();
            mWealthAccount.setBalanceInFens(balanceInFens);
        } catch (Exception e) {
            ToastHelper.toast("请输入正确的金额");
            return;
        }
        mWealthAccount.setNo(mEditNo.getText().toString());
        //wealthAccount.setOrganization(Organization.valueOf(mTextOrg.getText().toString()));
        mWealthAccount.setAlias(mEditAlias.getText().toString());
        if(mCheckCredit.isChecked()) {
            //TODO:
            //ToastHelper.toast("请输入信用卡相关信息");
            mWealthAccount.setType(AccountType.CREDIT);
            int billDay = Integer.parseInt(mEditBillDay.getText().toString());
            int repaymentDay = Integer.parseInt(mEditRepaymentDay.getText().toString());
            CreditCard creditCard = new CreditCard(mWealthAccount.getOrganization(), mWealthAccount.getNo(),
                    billDay, repaymentDay, mEditExpireDate.getText().toString());
            App.getInstance().getDaoSession().getCreditCardDao().insert(creditCard);
        }
        else if(mWealthAccount.getOrganization() == Organization.JD
                || mWealthAccount.getOrganization() == Organization.BAIDU
                || mWealthAccount.getOrganization() == Organization.ANT) {
            mWealthAccount.setType(AccountType.INTERNET);
        }
        else {
            mWealthAccount.setType(AccountType.DEBIT);
        }
        mWealthAccount.setUpdatedTime(new Date(System.currentTimeMillis()));
        App.getInstance().getDaoSession().getWealthAccountDao().insert(mWealthAccount);
        finish();
    }
}
