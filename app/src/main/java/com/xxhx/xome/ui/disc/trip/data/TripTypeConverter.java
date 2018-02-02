package com.xxhx.xome.ui.disc.trip.data;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xxhx on 2017/5/5.
 */

public class TripTypeConverter implements PropertyConverter<TripType, String> {
    @Override
    public TripType convertToEntityProperty(String databaseValue) {
        return TripType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(TripType entityProperty) {
        return entityProperty.name();
    }
}
