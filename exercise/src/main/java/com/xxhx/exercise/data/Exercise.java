package com.xxhx.exercise.data;

import com.amap.api.location.AMapLocation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by xxhx on 2017/7/21.
 */

@Entity
public class Exercise {
    @Id
    private Long id;
    private Date startTime;
    private Date endTime;
    private long duration;
    private double distance;
    private long stepCount;
    @Convert(columnType = String.class, converter = LongStringConverter.class)
    private List<Long> millsPerKM;
    @Convert(columnType = String.class, converter = IntegerStringConverter.class)
    private List<Integer> stepRecord;
    @Convert(columnType = String.class, converter = DoubleStringConverter.class)
    private List<Double> pathLatitude;
    @Convert(columnType = String.class, converter = DoubleStringConverter.class)
    private List<Double> pathLongitude;
    @Convert(columnType = String.class, converter = FloatStringConverter.class)
    private List<Float> pathSpeed;

    @Generated(hash = 1204967987)
    public Exercise(Long id, Date startTime, Date endTime, long duration,
            double distance, long stepCount, List<Long> millsPerKM,
            List<Integer> stepRecord, List<Double> pathLatitude,
            List<Double> pathLongitude, List<Float> pathSpeed) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.distance = distance;
        this.stepCount = stepCount;
        this.millsPerKM = millsPerKM;
        this.stepRecord = stepRecord;
        this.pathLatitude = pathLatitude;
        this.pathLongitude = pathLongitude;
        this.pathSpeed = pathSpeed;
    }

    @Generated(hash = 1537691247)
    public Exercise() {
    }

    @Keep
    public void addPathLocation(AMapLocation aMapLocation) {
        if(pathLatitude == null) {
            pathLatitude = new ArrayList<Double>();
        }
        if(pathLongitude == null) {
            pathLongitude = new ArrayList<Double>();
        }
        if(pathSpeed == null) {
            pathSpeed = new ArrayList<Float>();
        }
        pathLatitude.add(aMapLocation.getLatitude());
        pathLongitude.add(aMapLocation.getLongitude());
        pathSpeed.add(aMapLocation.getSpeed());
    }

    @Keep
    public boolean isValid() {
        if(duration < (5 * 60 * 1000)) return false;
        if(distance < 1000) return false;
        if (pathLatitude == null
                || pathLatitude.size() < 10
                || pathLongitude == null
                || pathLongitude.size() < 10
                || pathSpeed == null
                || pathSpeed.size() < 10) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getStepCount() {
        return this.stepCount;
    }

    public void setStepCount(long stepCount) {
        this.stepCount = stepCount;
    }

    public List<Long> getMillsPerKM() {
        return this.millsPerKM;
    }

    public void setMillsPerKM(List<Long> millsPerKM) {
        this.millsPerKM = millsPerKM;
    }

    public List<Integer> getStepRecord() {
        return this.stepRecord;
    }

    public void setStepRecord(List<Integer> stepRecord) {
        this.stepRecord = stepRecord;
    }

    public List<Double> getPathLatitude() {
        return this.pathLatitude;
    }

    public void setPathLatitude(List<Double> pathLatitude) {
        this.pathLatitude = pathLatitude;
    }

    public List<Double> getPathLongitude() {
        return this.pathLongitude;
    }

    public void setPathLongitude(List<Double> pathLongitude) {
        this.pathLongitude = pathLongitude;
    }

    public List<Float> getPathSpeed() {
        return this.pathSpeed;
    }

    public void setPathSpeed(List<Float> pathSpeed) {
        this.pathSpeed = pathSpeed;
    }
}
