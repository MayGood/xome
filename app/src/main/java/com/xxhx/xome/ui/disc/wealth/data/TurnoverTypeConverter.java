package com.xxhx.xome.ui.disc.wealth.data;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xxhx on 2017/4/12.
 */

public class TurnoverTypeConverter implements PropertyConverter<TurnoverType, String> {
    @Override
    public TurnoverType convertToEntityProperty(String databaseValue) {
        return TurnoverType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(TurnoverType entityProperty) {
        return entityProperty.name();
    }
}
