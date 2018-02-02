package com.xxhx.exercise.data;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xxhx on 2017/7/21.
 */

public class FloatStringConverter implements PropertyConverter<List<Float>, String> {
    @Override
    public List<Float> convertToEntityProperty(String databaseValue) {
        List<Float> results = new ArrayList<Float>();
        if(!TextUtils.isEmpty(databaseValue)) {
            List<String> list = Arrays.asList(databaseValue.split(","));
            if(list != null && list.size() > 0) {
                for(String data : list) {
                    results.add(Float.valueOf(data));
                }
            }
        }

        return results;
    }

    @Override
    public String convertToDatabaseValue(List<Float> entityProperty) {
        if(entityProperty == null) return null;

        StringBuilder buffer = new StringBuilder();
        Iterator<Float> it = entityProperty.iterator();
        while (it.hasNext()) {
            Float next = it.next();
            buffer.append(next.toString());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
