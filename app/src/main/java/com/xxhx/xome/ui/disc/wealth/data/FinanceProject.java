package com.xxhx.xome.ui.disc.wealth.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2017/4/5.
 */

@Entity
public class FinanceProject {
    private String name;

    @Generated(hash = 1582740746)
    public FinanceProject(String name) {
        this.name = name;
    }

    @Generated(hash = 671120902)
    public FinanceProject() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
