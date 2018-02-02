package com.xxhx.xome.ui.disc.wealth.data;

import java.text.DecimalFormat;
import java.util.Date;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by xxhx on 2017/4/6.
 */

@Entity
public class Turnover {
    @Id
    private Long id;
    private Date time;
    private long amountInFens;
    private String note;
    private Long accountId;
    @Convert(converter = TurnoverTypeConverter.class, columnType = String.class)
    private TurnoverType turnoverType;
    @Generated(hash = 1704548549)
    public Turnover(Long id, Date time, long amountInFens, String note, Long accountId,
            TurnoverType turnoverType) {
        this.id = id;
        this.time = time;
        this.amountInFens = amountInFens;
        this.note = note;
        this.accountId = accountId;
        this.turnoverType = turnoverType;
    }
    @Generated(hash = 1647914753)
    public Turnover() {
    }
    @Keep
    public String getFormattedAmount() {
        DecimalFormat format = new DecimalFormat("#,####");
        return String.format("%s.%02d", format.format(amountInFens / 100), Math.abs(amountInFens) % 100);
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public long getAmountInFens() {
        return this.amountInFens;
    }
    public void setAmountInFens(long amountInFens) {
        this.amountInFens = amountInFens;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Long getAccountId() {
        return this.accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public TurnoverType getTurnoverType() {
        return this.turnoverType;
    }
    public void setTurnoverType(TurnoverType turnoverType) {
        this.turnoverType = turnoverType;
    }
}
