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

public class LongStringConverter implements PropertyConverter<List<Long>, String> {
    @Override
    public List<Long> convertToEntityProperty(String databaseValue) {
        List<Long> results = new ArrayList<Long>();
        if(!TextUtils.isEmpty(databaseValue)) {
            List<String> list = Arrays.asList(databaseValue.split(","));
            if(list != null && list.size() > 0) {
                for(String data : list) {
                    results.add(Long.valueOf(data));
                }
            }
        }

        return results;
    }

    @Override
    public String convertToDatabaseValue(List<Long> entityProperty) {
        if(entityProperty == null) return null;

        StringBuilder buffer = new StringBuilder();
        Iterator<Long> it = entityProperty.iterator();
        while (it.hasNext()) {
            Long next = it.next();
            buffer.append(next.toString());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
