package com.xxhx.xome.ui.disc.trip.data;

import java.util.Date;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2017/5/5.
 */

@Entity
public class Trip {
    @Id
    private Long id;
    @Convert(converter = TripTypeConverter.class, columnType = String.class)
    private TripType type;
    private String no;
    private String seat;
    private Date time;
    private int durationInMinutes;
    private String stationFrom;
    private String stationTo;
    private String remark;
    @Generated(hash = 815012385)
    public Trip(Long id, TripType type, String no, String seat, Date time,
            int durationInMinutes, String stationFrom, String stationTo,
            String remark) {
        this.id = id;
        this.type = type;
        this.no = no;
        this.seat = seat;
        this.time = time;
        this.durationInMinutes = durationInMinutes;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.remark = remark;
    }
    @Generated(hash = 1047475835)
    public Trip() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TripType getType() {
        return this.type;
    }
    public void setType(TripType type) {
        this.type = type;
    }
    public String getNo() {
        return this.no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getSeat() {
        return this.seat;
    }
    public void setSeat(String seat) {
        this.seat = seat;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public int getDurationInMinutes() {
        return this.durationInMinutes;
    }
    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
    public String getStationFrom() {
        return this.stationFrom;
    }
    public void setStationFrom(String stationFrom) {
        this.stationFrom = stationFrom;
    }
    public String getStationTo() {
        return this.stationTo;
    }
    public void setStationTo(String stationTo) {
        this.stationTo = stationTo;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
