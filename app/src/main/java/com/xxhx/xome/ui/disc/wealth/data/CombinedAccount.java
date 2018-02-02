package com.xxhx.xome.ui.disc.wealth.data;

import java.util.List;

/**
 * Created by xxhx on 2017/5/11.
 */

public class CombinedAccount {
    private WealthAccount mWealthAccount;
    private CreditCard mCreditCard;
    private CreditBill mCurrentBill;
    private List<CreditBill> mUnpaidBills;

    public CombinedAccount(WealthAccount wealthAccount) {
        this.mWealthAccount = wealthAccount;
    }

    public CombinedAccount(WealthAccount wealthAccount, CreditCard creditCard,
            CreditBill currentBill, List<CreditBill> unpaidBills) {
        this.mWealthAccount = wealthAccount;
        this.mCreditCard = creditCard;
        this.mCurrentBill = currentBill;
        this.mUnpaidBills = unpaidBills;
    }

    public long getId() {
        return mWealthAccount.getId();
    }

    public Organization getOrganization() {
        return mWealthAccount.getOrganization();
    }

    public AccountType getType() {
        return mWealthAccount.getType();
    }

    public String getDisplayName() {
        return mWealthAccount.getDisplayName();
    }

    public String getFormattedBalance() {
        return mWealthAccount.getFormattedBalance();
    }

    public long getBalanceInFens() {
        return mWealthAccount.getBalanceInFens();
    }

    public List<CreditBill> getUnpaidBills() {
        return mUnpaidBills;
    }

    public CreditBill getCurrentBill() {
        return mCurrentBill;
    }
}
