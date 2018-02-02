package com.xxhx.xome.ui.disc.wealth.data;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xxhx on 2017/4/5.
 */

public class AccountTypeConverter implements PropertyConverter<AccountType, String> {
    @Override
    public AccountType convertToEntityProperty(String databaseValue) {
        return AccountType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(AccountType entityProperty) {
        return entityProperty.name();
    }
}
