package com.xxhx.xome.ui.disc.wealth.data;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2017/4/6.
 */

@Entity
public class CreditCard {
    @Convert(converter = OrganizationConverter.class, columnType = String.class)
    private Organization organization;
    private String no;
    private int billDay;
    private int repaymentDay;
    private String expireDate;
    @Generated(hash = 128342668)
    public CreditCard(Organization organization, String no, int billDay,
            int repaymentDay, String expireDate) {
        this.organization = organization;
        this.no = no;
        this.billDay = billDay;
        this.repaymentDay = repaymentDay;
        this.expireDate = expireDate;
    }
    @Generated(hash = 1860989810)
    public CreditCard() {
    }
    public Organization getOrganization() {
        return this.organization;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    public String getNo() {
        return this.no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public int getBillDay() {
        return this.billDay;
    }
    public void setBillDay(int billDay) {
        this.billDay = billDay;
    }
    public int getRepaymentDay() {
        return this.repaymentDay;
    }
    public void setRepaymentDay(int repaymentDay) {
        this.repaymentDay = repaymentDay;
    }
    public String getExpireDate() {
        return this.expireDate;
    }
    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
