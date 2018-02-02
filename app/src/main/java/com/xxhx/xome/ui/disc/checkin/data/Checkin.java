package com.xxhx.xome.ui.disc.checkin.data;

import java.util.Date;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2017/8/1.
 */

@Entity
public class Checkin {
    @Id
    private Long id;
    private long date;
    private Date time;
    private boolean checkout;
    @Generated(hash = 977399579)
    public Checkin(Long id, long date, Date time, boolean checkout) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.checkout = checkout;
    }
    @Generated(hash = 1391761160)
    public Checkin() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public boolean getCheckout() {
        return this.checkout;
    }
    public void setCheckout(boolean checkout) {
        this.checkout = checkout;
    }
}
