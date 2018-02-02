package com.xxhx.xome;

import android.app.Activity;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.TextView;
import com.xxhx.xome.config.Identity;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.helper.FingerprintHelper;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.manager.WeiboManager;
import com.xxhx.xome.ui.MainActivity;
import com.xxhx.xome.util.XLoger;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SplashActivity extends Activity implements FingerprintHelper.Callback {
    private static final long SPLASH_TIME = 400;

    private TextView mDateView;
    private View mFpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDateView = (TextView) findViewById(R.id.date);
        initDateView();
        mFpView = findViewById(R.id.fp);
        mFpView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                App.getInstance().setIdentity(Identity.Visitor);
                gotoMainActivity();
                return true;
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void initDateView() {
        Calendar calendar = Calendar.getInstance();
        int displayMonth = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDateView.setText(String.format("%d %02d", displayMonth, day));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread("splash-init") {

            @Override
            public void run() {
                long st = System.nanoTime();
                WeiboManager.getInstance();
                //CrashReport.testJavaCrash();
                //WeiboManager manager = WeiboManager.getInstance(getApplicationContext());
                //EmotionManager.getInstance(getApplicationContext());
                //GlobalParams globalParams = GlobalParams.getInstance(getApplicationContext());
                //if(globalParams.isRemindCheckin()) {
                //    CheckinUtil.setAlarm(getApplicationContext());
                //}
                //ChatManager.getInstance(getApplicationContext());
                //if(globalParams.getBooleanValue(GlobalParams.KEY_INTRANET_CHAT, false)) {
                //    Intent serviceIntent = new Intent(getApplicationContext(), Hello250Service.class);
                //    startService(serviceIntent);
                //}
                //ImageLoaderUtil.config(getApplicationContext());
                long et = System.nanoTime();
                long sleep = SPLASH_TIME - (et - st) / 1000000;
                if(sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //SplashActivity.this.runOnUiThread(new Runnable() {
                //
                //    @Override
                //    public void run() {
                //        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //        startActivity(intent);
                //        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //        finish();
                //    }
                //});
            }
        }.start();
        initFingerPrintListener();
    }

    private void initFingerPrintListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
            FingerprintHelper fingerprintHelper = new FingerprintHelper(getSystemService(FingerprintManager.class), this);
            fingerprintHelper.startListening(null);
        }
    }

    @Override
    public void onAuthenticated() {
        App.getInstance().setIdentity(Identity.Manager);
        gotoMainActivity();
    }

    @Override
    public void onError() {
        App.getInstance().setIdentity(Identity.Visitor);
        gotoMainActivity();
    }
}
