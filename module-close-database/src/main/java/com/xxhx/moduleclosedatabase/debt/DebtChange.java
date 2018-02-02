package com.xxhx.moduleclosedatabase.debt;

import java.util.Date;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2018/1/23.
 */

@Entity
public class DebtChange {
    @Id
    private Long id;
    private Long debtId;
    private long changeInFens;
    private Date date;
    private String notes;

    @Generated(hash = 466793357)
    public DebtChange(Long id, Long debtId, long changeInFens, Date date,
            String notes) {
        this.id = id;
        this.debtId = debtId;
        this.changeInFens = changeInFens;
        this.date = date;
        this.notes = notes;
    }

    @Generated(hash = 255933230)
    public DebtChange() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDebtId() {
        return this.debtId;
    }

    public void setDebtId(Long debtId) {
        this.debtId = debtId;
    }

    public long getChangeInFens() {
        return this.changeInFens;
    }

    public void setChangeInFens(long changeInFens) {
        this.changeInFens = changeInFens;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
