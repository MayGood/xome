package com.xxhx.xome.ui.disc.wealth.data;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xxhx on 2017/4/5.
 */

public class OrganizationConverter implements PropertyConverter<Organization, String> {
    @Override
    public Organization convertToEntityProperty(String databaseValue) {
        return Organization.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(Organization entityProperty) {
        return entityProperty.name();
    }
}
