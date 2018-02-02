package com.xxhx.moduleclosedatabase.debt;

import java.text.DecimalFormat;
import java.util.Date;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by xxhx on 2018/1/23.
 */

@Entity
public class Debt {
    @Id
    private Long id;
    private String creditor;
    private Date dueDate;
    private Date addedDate;
    private Date lastUpdateDate;
    private long amountInFens;
    private long baseInFens;

    @Generated(hash = 83037100)
    public Debt(Long id, String creditor, Date dueDate, Date addedDate,
            Date lastUpdateDate, long amountInFens, long baseInFens) {
        this.id = id;
        this.creditor = creditor;
        this.dueDate = dueDate;
        this.addedDate = addedDate;
        this.lastUpdateDate = lastUpdateDate;
        this.amountInFens = amountInFens;
        this.baseInFens = baseInFens;
    }

    @Generated(hash = 488411483)
    public Debt() {
    }

    @Keep
    public String getFormattedAmount() {
        DecimalFormat format = new DecimalFormat("#,####");
        return String.format("%s.%02d", format.format(amountInFens / 100), Math.abs(amountInFens) % 100);
    }

    @Keep
    public String getFormattedBase() {
        DecimalFormat format = new DecimalFormat("#,####");
        return String.format("%s.%02d", format.format(baseInFens / 100), Math.abs(baseInFens) % 100);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditor() {
        return this.creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getAddedDate() {
        return this.addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public long getAmountInFens() {
        return this.amountInFens;
    }

    public void setAmountInFens(long amountInFens) {
        this.amountInFens = amountInFens;
    }

    public long getBaseInFens() {
        return this.baseInFens;
    }

    public void setBaseInFens(long baseInFens) {
        this.baseInFens = baseInFens;
    }
}
