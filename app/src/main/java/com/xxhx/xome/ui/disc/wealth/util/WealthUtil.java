package com.xxhx.xome.ui.disc.wealth.util;

import com.xxhx.xome.App;
import com.xxhx.xome.data.CreditBillDao;
import com.xxhx.xome.ui.disc.wealth.data.CreditBill;
import com.xxhx.xome.util.CommonUtil;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xxhx on 2017/5/3.
 */

public class WealthUtil {

    public static int getWarningCount() {
        int count = 0;
        List<CreditBill> unPaidBills = App.getInstance().getDaoSession().getCreditBillDao()
                .queryBuilder()
                .where(CreditBillDao.Properties.PaidOff.eq(false))
                .list();
        for(CreditBill bill : unPaidBills) {
            String deadline = bill.getDeadline();
            if(deadline != null && deadline.length() == 8) {
                try {
                    int year = Integer.parseInt(deadline.substring(0, 4));
                    int month = Integer.parseInt(deadline.substring(4, 6));
                    int dayOfMonth = Integer.parseInt(deadline.substring(6));
                    Calendar billCalendar = Calendar.getInstance();
                    billCalendar.set(year, month, dayOfMonth);
                    if(CommonUtil.getRelativeDays(billCalendar.getTime().getTime()) > -7) {
                        count++;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }
}
