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

public class IntegerStringConverter implements PropertyConverter<List<Integer>, String> {
    @Override
    public List<Integer> convertToEntityProperty(String databaseValue) {
        List<Integer> results = new ArrayList<Integer>();
        if(!TextUtils.isEmpty(databaseValue)) {
            List<String> list = Arrays.asList(databaseValue.split(","));
            if(list != null && list.size() > 0) {
                for(String data : list) {
                    results.add(Integer.valueOf(data));
                }
            }
        }

        return results;
    }

    @Override
    public String convertToDatabaseValue(List<Integer> entityProperty) {
        if(entityProperty == null) return null;

        StringBuilder buffer = new StringBuilder();
        Iterator<Integer> it = entityProperty.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            buffer.append(next.toString());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
