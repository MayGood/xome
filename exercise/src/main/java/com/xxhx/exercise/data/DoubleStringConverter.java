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

public class DoubleStringConverter implements PropertyConverter<List<Double>, String> {
    @Override
    public List<Double> convertToEntityProperty(String databaseValue) {
        List<Double> results = new ArrayList<Double>();
        if(!TextUtils.isEmpty(databaseValue)) {
            List<String> list = Arrays.asList(databaseValue.split(","));
            if(list != null && list.size() > 0) {
                for(String data : list) {
                    results.add(Double.valueOf(data));
                }
            }
        }

        return results;
    }

    @Override
    public String convertToDatabaseValue(List<Double> entityProperty) {
        if(entityProperty == null) return null;

        StringBuilder buffer = new StringBuilder();
        Iterator<Double> it = entityProperty.iterator();
        while (it.hasNext()) {
            Double next = it.next();
            buffer.append(next.toString());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
