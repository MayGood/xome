package com.xxhx.xome.ui.disc.note;

import java.util.Date;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2018/2/3.
 */

@Entity
public class Memo {
    @Id
    private Long id;
    private String content;
    private Date createTime;
    private Date modifyTime;
    @Generated(hash = 913363959)
    public Memo(Long id, String content, Date createTime, Date modifyTime) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
    @Generated(hash = 1901232184)
    public Memo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getModifyTime() {
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
