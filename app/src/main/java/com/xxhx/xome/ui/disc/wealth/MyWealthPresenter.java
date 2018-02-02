package com.xxhx.xome.ui.disc.wealth;

import com.xxhx.xome.App;
import com.xxhx.xome.data.CreditBillDao;
import com.xxhx.xome.data.CreditCardDao;
import com.xxhx.xome.data.WealthAccountDao;
import com.xxhx.xome.ui.disc.wealth.data.AccountType;
import com.xxhx.xome.ui.disc.wealth.data.CombinedAccount;
import com.xxhx.xome.ui.disc.wealth.data.CreditBill;
import com.xxhx.xome.ui.disc.wealth.data.CreditCard;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by xxhx on 2017/4/5.
 */

public class MyWealthPresenter implements MyWealthContract.Presenter {

    private MyWealthContract.View mView;

    public MyWealthPresenter(MyWealthContract.View view) {
        mView = checkNotNull(view);
    }

    private CombinedAccount queryCreditAccount(WealthAccount wealthAccount) {
        CreditCard creditCard = App.getInstance().getDaoSession().getCreditCardDao().queryBuilder()
                .where(CreditCardDao.Properties.No.eq(wealthAccount.getNo()))
                .list().get(0);
        List<CreditBill> unpaidBills = App.getInstance().getDaoSession().getCreditBillDao().queryBuilder()
                .where(CreditBillDao.Properties.CreditNo.eq(wealthAccount.getNo())
                        , CreditBillDao.Properties.PaidOff.eq(false))
                .list();
        CreditBill currentBill = null;
        Calendar calendarNow = Calendar.getInstance();
        int year = calendarNow.get(Calendar.YEAR);
        int month = calendarNow.get(Calendar.MONTH);
        int dayOfMonth = calendarNow.get(Calendar.DAY_OF_MONTH);
        if(dayOfMonth >= creditCard.getBillDay()) {
            List<CreditBill> creditBills = App.getInstance().getDaoSession().getCreditBillDao()
                    .queryBuilder()
                    .where(CreditBillDao.Properties.CreditNo.eq(wealthAccount.getNo())
                            , CreditBillDao.Properties.Year.eq(year)
                            , CreditBillDao.Properties.Month.eq(month))
                    .list();
            if(creditBills != null && creditBills.size() > 0) {
                currentBill = creditBills.get(0);
            }
        }
        else if(month > 0) {
            List<CreditBill> creditBills = App.getInstance().getDaoSession().getCreditBillDao()
                    .queryBuilder()
                    .where(CreditBillDao.Properties.CreditNo.eq(wealthAccount.getNo()),
                            CreditBillDao.Properties.Year.eq(year),
                            CreditBillDao.Properties.Month.eq(month - 1))
                    .list();
            if(creditBills != null && creditBills.size() > 0) {
                currentBill = creditBills.get(0);
            }
        }
        else {
            List<CreditBill> creditBills = App.getInstance().getDaoSession().getCreditBillDao()
                    .queryBuilder()
                    .where(CreditBillDao.Properties.CreditNo.eq(wealthAccount.getNo()),
                            CreditBillDao.Properties.Year.eq(year - 1),
                            CreditBillDao.Properties.Month.eq(Calendar.DECEMBER))
                    .list();
            if(creditBills != null && creditBills.size() > 0) {
                currentBill = creditBills.get(0);
            }
        }
        return new CombinedAccount(wealthAccount, creditCard, currentBill, unpaidBills);
    }

    @Override
    public void subscribe() {
        List<WealthAccount> wealthAccounts = App.getInstance().getDaoSession().getWealthAccountDao()
                .queryBuilder()
                .orderDesc(WealthAccountDao.Properties.Type)
                .orderAsc(WealthAccountDao.Properties.AddedTime)
                .list();
        List<CombinedAccount> combinedAccounts = new ArrayList<CombinedAccount>();
        if(wealthAccounts != null) {
            for(WealthAccount wealthAccount : wealthAccounts) {
                if(wealthAccount.getType() == AccountType.CREDIT) {
                    combinedAccounts.add(queryCreditAccount(wealthAccount));
                }
                else {
                    combinedAccounts.add(new CombinedAccount(wealthAccount));
                }
            }
        }
        mView.showWealthAccounts(combinedAccounts);
    }

    @Override
    public void unsubscribe() {

    }
}
