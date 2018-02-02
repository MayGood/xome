package com.xxhx.xome.ui.disc.wealth.data;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xxhx on 2017/4/5.
 */

public class CurrencyConverter implements PropertyConverter<Currency, String> {
    @Override
    public Currency convertToEntityProperty(String databaseValue) {
        return Currency.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(Currency entityProperty) {
        return entityProperty.name();
    }
}
