package com.xxhx.xome.ui.disc.wealth.data;

/**
 * Created by xxhx on 2017/4/5.
 */

public enum Organization {
    ICBC("工商银行"), CMB("招商银行"), BOC("中国银行"), PSBC("邮储银行"), BJBANK("北京银行"), CITIC("中信银行"),
    ANT("蚂蚁金服"), BAIDU("百度金融"), JD("京东金融");

    private String name;
    private Organization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
