package com.xxhx.xome.ui.disc.note;

import java.util.Date;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2018/2/3.
 */

@Entity
public class Notebook {
    @Id
    private Long id;
    private int type;
    private String name;
    private Date createTime;
    private Date modifyTime;
    private String path;    // 云端：笔记本的路径
    private int notesNum;
    @Generated(hash = 851572468)
    public Notebook(Long id, int type, String name, Date createTime,
            Date modifyTime, String path, int notesNum) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.path = path;
        this.notesNum = notesNum;
    }
    @Generated(hash = 1348176405)
    public Notebook() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public int getNotesNum() {
        return this.notesNum;
    }
    public void setNotesNum(int notesNum) {
        this.notesNum = notesNum;
    }

    /**
     * “path” : “/4AF64012E9864C”, // 笔记本的路径

     “name” : “笔记本1”, // 笔记本的名称

     “notes_num” : “3” // 该笔记本中笔记的数目

     “create_time” : “1323310917” // 笔记本的创建时间，单位秒

     “modify_time” : “1323310949” // 笔记本的最后修改时间，单位秒
     */
}
