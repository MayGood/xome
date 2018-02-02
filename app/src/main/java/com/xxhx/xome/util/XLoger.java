package com.xxhx.xome.util;

import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xxhx on 2017/8/18.
 */

public class XLoger {

    public static void log(long time, String errorType, String errorMessage,
            String errorStack) {
        try {
            File xxhxDir = new File(Environment.getExternalStorageDirectory(), "xxhx");
            if(!xxhxDir.exists() || !xxhxDir.isDirectory()) {
                xxhxDir.mkdir();
            }
            File logDir = new File(xxhxDir, "crash");
            if(!logDir.exists() || !logDir.isDirectory()) {
                logDir.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            File logFile = new File(logDir, sdf.format(new Date(time)) + ".txt");
            FileWriter fw = new FileWriter(logFile, true);
            fw.append(errorType + "\n\n");
            fw.append(errorMessage + "\n\n");
            fw.append(errorStack + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(long time, String text, String fileName) {
        try {
            File logDir = new File(Environment.getExternalStorageDirectory(), "0_log");
            if(!logDir.exists() || !logDir.isDirectory()) {
                logDir.mkdir();
            }
            File logFile = new File(logDir, fileName + ".txt");
            FileWriter fw = new FileWriter(logFile, true);
            fw.append(formatTime(time) + "\t" + text + "\r\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(long time, String text) {
        try {
            File logDir = new File(Environment.getExternalStorageDirectory(), "0_log");
            if(!logDir.exists() || !logDir.isDirectory()) {
                logDir.mkdir();
            }
            File logFile = new File(logDir, "xloger.txt");
            FileWriter fw = new FileWriter(logFile, true);
            fw.append(formatTime(time) + "\t" + text + "\r\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }
}
