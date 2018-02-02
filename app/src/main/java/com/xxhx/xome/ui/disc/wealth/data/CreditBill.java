package com.xxhx.xome.ui.disc.wealth.data;

import java.text.DecimalFormat;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by xxhx on 2017/4/13.
 */

@Entity(generateConstructors = false)
public class CreditBill {
    @Id
    private Long id;
    private String creditNo;
    private int year;
    private int month;
    private long billInFens;
    private boolean paidOff;
    @Convert(converter = CurrencyConverter.class, columnType = String.class)
    private Currency currency;
    private String deadline;
    public CreditBill() {
    }
    @Keep
    public String getFormattedBill() {
        DecimalFormat format = new DecimalFormat("#,####");
        return String.format("%s.%02d", format.format(billInFens / 100), Math.abs(billInFens) % 100);
    }
    public String getCreditNo() {
        return this.creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public int getYear() {
        return this.year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return this.month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public long getBillInFens() {
        return this.billInFens;
    }
    public void setBillInFens(long billInFens) {
        this.billInFens = billInFens;
    }
    public boolean getPaidOff() {
        return this.paidOff;
    }
    public void setPaidOff(boolean paidOff) {
        this.paidOff = paidOff;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeadline() {
        return this.deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
