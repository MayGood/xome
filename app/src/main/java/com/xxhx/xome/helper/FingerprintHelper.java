package com.xxhx.xome.helper;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xxhx on 2017/2/16.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

    private final FingerprintManager mFingerprintManager;
    private final Callback mCallback;
    private CancellationSignal mCancellationSignal;

    public FingerprintHelper(FingerprintManager fingerprintManager, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mCallback = callback;
    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        //mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager
                .authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if(mCallback != null) {
            mCallback.onAuthenticated();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        if(mCallback != null) {
            mCallback.onError();
        }
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        mCallback.onError();
    }

    public interface Callback {

        void onAuthenticated();

        void onError();
    }
}
