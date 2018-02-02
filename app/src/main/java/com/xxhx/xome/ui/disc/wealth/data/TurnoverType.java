package com.xxhx.xome.ui.disc.wealth.data;

/**
 * Created by xxhx on 2017/4/12.
 */

public enum TurnoverType {
    TRANSFER("转账", 3), INCOME("收入", 1), WAGE("工资", 1), GAIN("收益", 1), OUTCOME("支出", 2), CONSUMPTION("消费", 2);

    private String type;
    private int scope;
    private TurnoverType(String type, int scope) {
        this.type = type;
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public boolean isWealthIn() {
        if((scope & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isWealthOut() {
        if((scope & 2) == 2) {
            return true;
        }
        return false;
    }
}
