package com.xxhx.xome.test;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.view.TestImageView;
import com.xxhx.xome.view.XImageView;
import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class TestActivity extends BaseActivity {

    private XImageView testImageView;

    private View toggleBar;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test);

        //testImageView = (XImageView) findViewById(R.id.test);
        //File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/test/4.png");
        //testImageView.setImageFile(file);
        //Intent intent = new Intent();
        //intent.setComponent(new ComponentName("com.icbc.im", "com.icbc.im.ui.activity.mainframe.MainFrameActivity"));
        //startActivity(intent);

        toggleBar = findViewById(R.id.toggle_bar);

        DisplayMetrics displayMetrics = App.getInstance().getDisplayMetrics();
        Log.i("testd", "displayMetrics:" + String.valueOf(displayMetrics.density));
        int tt = (int) (240 * displayMetrics.density);
        Log.i("testd", "tt:" + String.valueOf(tt));

        testll();
    }

    private void testll() {
        View view = findViewById(R.id.testll);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash_april);
        CenterCropDrawable drawable = new CenterCropDrawable(bitmap);
        drawable.setBounds(1200, 2000, 1800, 2600);
        view.setBackground(drawable);
    }

    public void test0(View view) {
        int vc = view.getLeft() / 2 + view.getRight() / 2 - toggleBar.getWidth() / 2;
        toggleBar.animate().translationX(vc).setDuration(200).start();

        ToastHelper.toast(getMacAddress());
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface nif : all) {
                if(nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] mac = nif.getHardwareAddress();
                    if(mac == null) {
                        return "";
                    }

                    StringBuilder builder = new StringBuilder();
                    for(byte b : mac) {
                        builder.append(String.format("%02X", b));
                    }
                    return builder.toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
