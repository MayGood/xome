package com.xxhx.exercise;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.xxhx.exercise.data.DaoMaster;
import com.xxhx.exercise.data.DaoSession;
import com.xxhx.exercise.data.Exercise;
import com.xxhx.exercise.data.ExerciseDao;
import com.xxhx.exercise.data.ExerciseDbHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xxhx on 2017/7/25.
 */

public class ExerciseRecordService extends Service implements SensorEventListener {
    private static final String DEFAULT_DB_NAME = "exercise-db";

    /** Keeps track of all current registered clients. */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    /** Holds last value set by a client. */
    int mValue = 0;

    /**
     * Command to the service to register a client, receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client where callbacks should be sent.
     */
    static final int MSG_REGISTER_CLIENT = 1;

    /**
     * Command to the service to unregister a client, ot stop receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client as previously given with MSG_REGISTER_CLIENT.
     */
    static final int MSG_UNREGISTER_CLIENT = 2;

    /**
     * Command to service to set a new value.  This can be sent to the
     * service to supply a new value, and will be sent by the service to
     * any registered clients with the new value.
     */
    static final int MSG_CHECK_STATUS = 3;
    static final int MSG_START_RECORD = 4;
    static final int MSG_END_RECORD = 5;
    static final int MSG_UI_LOCATION = 6;
    static final int MSG_UI_STEPS = 7;
    static final int MSG_UI_DURATION = 8;

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_CHECK_STATUS:
                    mValue = mExercise == null ? 0 : 1;
                    for (int i=mClients.size()-1; i>=0; i--) {
                        try {
                            mClients.get(i).send(Message.obtain(null,
                                    MSG_CHECK_STATUS, mValue, 0));
                        } catch (RemoteException e) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                case MSG_START_RECORD:
                    startRecord();
                    break;
                case MSG_END_RECORD:
                    stopRecord();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ExerciseDao mExerciseDao;

    private boolean mRecording;

    private Exercise mExercise;

    private int mSecsPerKm;

    private long mDuration;
    private long mLastResumeTime;
    private List<Long> mMilecostRecords;
    private List<Integer> mStepRecords;

    private long mLastStepCount;
    private int mLastStepPeriod;
    private int mLastStepPeriodCount;

    private AMapLocation mLastLocation;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(mExercise != null && aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mExercise.addPathLocation(aMapLocation);
                if(mLastLocation != null) {
                    LatLng latLng1 = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    LatLng latLng2 = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    float distance = AMapUtils.calculateLineDistance(latLng1,latLng2);
                    if(distance > 0) {
                        mSecsPerKm =
                                (int) ((aMapLocation.getTime() - mLastLocation.getTime()) / distance);
                    }

                    mExercise.setDistance(mExercise.getDistance() + distance);
                    int kiloMeters = (int) (mExercise.getDistance() / 1000);
                    long duration = getDuration();
                    while(kiloMeters > mMilecostRecords.size()) {
                        mMilecostRecords.add(duration);
                    }
                }
                mExerciseDao.update(mExercise);
                mLastLocation = aMapLocation;
                updateLocation(aMapLocation.getGpsAccuracyStatus(), mSecsPerKm, mExercise.getDistance());
            }
        }
    };

    @Override
    public void onCreate() {
        String path = new File(getExternalFilesDir("db"), DEFAULT_DB_NAME).getAbsolutePath();
        ExerciseDbHelper helper = new ExerciseDbHelper(this, path);
        DaoSession daoSession = new DaoMaster(helper.getWritableDb()).newSession();
        mExerciseDao = daoSession.getExerciseDao();

        mRecording = false;
        initLocationClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.small_notification);
        builder.setContentTitle("运动中");
        builder.setContentText("点击可返回记录页面");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setShowWhen(false);
        }
        Intent notificationIntent = new Intent(this, ExerciseRecordActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        startForeground(12, builder.build());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        mLocationClient.onDestroy();
    }

    public boolean isRecording() {
        return mRecording;
    }

    public void startRecord() {
        mRecording = true;
        mExercise = new Exercise();
        mExercise.setStartTime(new Date());
        long id = mExerciseDao.insert(mExercise);
        mExercise.setId(id);
        mDuration = 0;
        mLastResumeTime = mExercise.getStartTime().getTime();
        mMilecostRecords = new ArrayList<Long>();
        mStepRecords = new ArrayList<Integer>();
        initStepCounter();
    }

    public void stopRecord() {
        mExercise.setEndTime(new Date());
        mExercise.setDuration(getDuration());

        int period = (int) (getDuration() / (60 * 1000));
        while (mStepRecords.size() < period) {
            if(mStepRecords.size() == mLastStepPeriod) {
                mStepRecords.add(mLastStepPeriodCount);
            }
            else {
                mStepRecords.add(0);
            }
        }
        mExercise.setStepRecord(mStepRecords);
        mExercise.setMillsPerKM(mMilecostRecords);
        //if(mExercise.isValid()) {
            mExerciseDao.update(mExercise);
        //}
        //else {
        //    mExerciseDao.delete(mExercise);
        //    Toast.makeText(this, "运动时间或距离太短", Toast.LENGTH_SHORT).show();
        //}
        mLocationClient.stopLocation();
        stopForeground(true);
        stopSelf();
    }

    private void updateLocation(int gpsAccuracyStatus, int secsPerKm, double distance) {
        for (int i=mClients.size()-1; i>=0; i--) {
            try {
                mClients.get(i)
                        .send(Message.obtain(null, MSG_UI_LOCATION, gpsAccuracyStatus, secsPerKm,
                                distance));
            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                // we are going through the list from back to front
                // so this is safe to do inside the loop.
                mClients.remove(i);
            }
        }
    }

    private void updateSteps(long steps) {
        for (int i=mClients.size()-1; i>=0; i--) {
            try {
                mClients.get(i)
                        .send(Message.obtain(null, MSG_UI_STEPS, (int) steps, 0));
            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                // we are going through the list from back to front
                // so this is safe to do inside the loop.
                mClients.remove(i);
            }
        }
    }

    public long getDuration() {
        return mDuration + (System.currentTimeMillis() - mLastResumeTime);
    }

    public long getStepCount() {
        return mExercise.getStepCount();
    }

    public Exercise getExercise() {
        return mExercise;
    }

    private void initLocationClient() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        mLocationOption.setInterval(5000);
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void initStepCounter() {
        mLastStepPeriod = (int) (getDuration() / (60 * 1000));
        mLastStepCount = Long.MAX_VALUE;
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values != null && event.values.length > 0) {
            long value = (long) event.values[0];
            int newStepCount = 0;
            if(mLastStepCount < value) {
                newStepCount = (int) (value - mLastStepCount);
                int period = (int) (getDuration() / (60 * 1000));
                if(period == mLastStepPeriod) {
                    mLastStepPeriodCount += newStepCount;
                }
                else {
                    while (mStepRecords.size() < mLastStepPeriod) {
                        mStepRecords.add(0);
                    }
                    mStepRecords.add(mLastStepPeriodCount);
                    mLastStepPeriod = period;
                    mLastStepPeriodCount = newStepCount;
                }
            }
            long stepCount = mExercise.getStepCount() + newStepCount;
            mExercise.setStepCount(stepCount);
            mLastStepCount = value;
            updateSteps(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}
