package com.xxhx.moduleclosedatabase.test;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2018/1/23.
 */

@Entity
public class Test {
    @Id
    private Long id;
    private String name;
    @Generated(hash = 294966613)
    public Test(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 372557997)
    public Test() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
