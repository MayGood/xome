package com.xxhx.xome.ui.disc.trip.data;

/**
 * Created by xxhx on 2017/5/6.
 */

public class Station {
    private String name;
    private String arriveTime;
    private String leaveTime;
    private int stayMinutes;

    public Station(String name, String arriveTime, String leaveTime, int stayMinutes) {
        this.name = name;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;
        this.stayMinutes = stayMinutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getStayMinutes() {
        return stayMinutes;
    }

    public void setStayMinutes(int stayMinutes) {
        this.stayMinutes = stayMinutes;
    }
}
