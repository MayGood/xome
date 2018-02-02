package com.xxhx.exercise;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.xxhx.exercise.view.CircleColorDrawable;
import com.xxhx.exercise.view.CountDownView;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExerciseRecordActivity extends AppCompatActivity {
    /** Messenger for communicating with service. */
    Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    boolean mIsBound;

    private View mCountdownLayout;
    private CountDownView mCountDownView;

    private ImageView mGpsStatusView;
    private TextView mDistanceView;
    private TextView mMilecostView;
    private TextView mDurationView;
    private TextView mStepCountView;

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ExerciseRecordService.MSG_CHECK_STATUS:
                    switch (msg.arg1) {
                        case 0:
                            mCountdownLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCountDownView.startCountDown(3, new CountDownView.OnCountDownListener() {
                                        @Override
                                        public void onFinished() {
                                            hideCountdownLayout();
                                        }
                                    });
                                }
                            }, 500);
                            break;
                        case 1:
                            mCountdownLayout.setVisibility(View.GONE);
                            initScheduledExecutor();
                            break;
                    }
                    break;

                case ExerciseRecordService.MSG_UI_LOCATION:
                    updateLocationUI(msg.arg1, msg.arg2, (Double) msg.obj);
                    break;
                case ExerciseRecordService.MSG_UI_STEPS:
                    updateStepsUI(msg.arg1);
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(mIsBound){
                        updateUi();
                    }
                    break;
            }
        }
    };

    private ScheduledThreadPoolExecutor mScheduledExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        mCountdownLayout = findViewById(R.id.l_countdown);
        mCountDownView = (CountDownView) findViewById(R.id.countdown);

        mGpsStatusView = (ImageView) findViewById(R.id.gps);
        mDistanceView = (TextView) findViewById(R.id.distance);
        mMilecostView = (TextView) findViewById(R.id.milecost);
        mDurationView = (TextView) findViewById(R.id.duration);
        mStepCountView = (TextView) findViewById(R.id.step_count);

        startRecordService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, ExerciseRecordService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
            if(!mScheduledExecutor.isShutdown()) {
                mScheduledExecutor.shutdown();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void startRecordService() {
        Intent intent = new Intent(this, ExerciseRecordService.class);
        startService(intent);
    }

    private void updateLocationUI(int gpsAccuracyStatus, int secsPerKm, double distance) {
        mDistanceView.setText(String.format("%.02f", distance / 1000));
        switch (gpsAccuracyStatus) {
            case AMapLocation.GPS_ACCURACY_GOOD:
                mGpsStatusView.setImageResource(R.drawable.run_gps_good_signal);
                break;
            case AMapLocation.GPS_ACCURACY_BAD:
                mGpsStatusView.setImageResource(R.drawable.run_gps_bad_signal);
                break;
            case AMapLocation.GPS_ACCURACY_UNKNOWN:
                mGpsStatusView.setImageResource(R.drawable.run_gps_no_signal);
                break;
        }
        if(secsPerKm > 3600) {
            mMilecostView.setText("--");
        }
        else {
            mMilecostView.setText(String.format("%02d\'%02d\"", secsPerKm / 60, secsPerKm % 60));
        }
    }

    private void updateStepsUI(int steps) {
        mStepCountView.setText(Integer.toString(steps));
    }

    private void updateUi() {
        //long duration = mService.getDuration();
        //mDurationView.setText(String.format("%02d:%02d:%02d", duration / (60 * 60 * 1000), (duration / (60 * 1000)) % 60, (duration / 1000) % 60));
        //mStepCountView.setText(Long.toString(mService.getStepCount()));
    }

    private void initScheduledExecutor() {
        mScheduledExecutor = new ScheduledThreadPoolExecutor(1);
        mScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        end(null);
    }

    public void end(View view) {
        if(mIsBound) {
            try {
                Message msg = Message.obtain(null, ExerciseRecordService.MSG_END_RECORD);
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
        }
        finish();
    }

    private void start() {
        try {
            Message msg = Message.obtain(null, ExerciseRecordService.MSG_START_RECORD);
            mService.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even
            // do anything with it; we can count on soon being
            // disconnected (and then reconnected if it can be restarted)
            // so there is no need to do anything here.
        }
    }

    private void hideCountdownLayout() {
        int width = mCountdownLayout.getWidth();
        int height = mCountdownLayout.getHeight();
        float scale = (float) (Math.sqrt(width * width + height * height) / 2);
        mCountdownLayout.setBackground(new CircleColorDrawable(getResources().getColor(R.color.exercise)));
        mCountdownLayout.setScaleX(scale);
        mCountdownLayout.setScaleY(scale);
        mCountdownLayout.animate()
                .scaleX(0)
                .scaleY(0)
                .translationY(height * 2)
                .setDuration(700)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCountdownLayout.setVisibility(View.GONE);
                        if(mIsBound) {
                            start();
                            initScheduledExecutor();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        ExerciseRecordService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

                // Give it some value as an example.
                msg = Message.obtain(null, ExerciseRecordService.MSG_CHECK_STATUS);
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mIsBound = false;
        }
    };

}
