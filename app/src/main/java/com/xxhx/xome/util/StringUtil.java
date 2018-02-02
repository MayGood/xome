package com.xxhx.xome.util;

import android.text.TextUtils;
import android.text.format.DateUtils;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created by xxhx on 2017/4/5.
 */

public class StringUtil {
    public static String getMaskedString(String ori) {
        if(ori != null && ori.length() >= 12) {
            return ori.substring(0, 6) + "****" + ori.substring(ori.length() - 4);
        }
        return ori;
    }

    public static String getFormattedBankCardNo(String bankNo) {
        String originalBankNo = null;
        String formattedBankNo;
        if(bankNo != null) {
            originalBankNo = bankNo.replaceAll("\\s*", "");
        }
        if(!TextUtils.isEmpty(originalBankNo) && originalBankNo.length() > 10) {
            int length = originalBankNo.length();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length; i++) {
                builder.append(originalBankNo.charAt(i));
                if(i != (length - 1) && i % 4 == 3) {
                    builder.append(" ");
                }
            }
            formattedBankNo = builder.toString();
        }
        else {
            formattedBankNo = originalBankNo;
        }
        return formattedBankNo;
    }

    public static String inputStreamToString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
