package com.xxhx.xome.ui.disc.checkin.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2017/8/17.
 */

@Entity
public class DateMark {
    @Id
    private long date;
    private boolean workday;
    private String info;
    @Generated(hash = 2117027616)
    public DateMark(long date, boolean workday, String info) {
        this.date = date;
        this.workday = workday;
        this.info = info;
    }
    @Generated(hash = 1674937761)
    public DateMark() {
    }
    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public boolean getWorkday() {
        return this.workday;
    }
    public void setWorkday(boolean workday) {
        this.workday = workday;
    }
    public String getInfo() {
        return this.info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
