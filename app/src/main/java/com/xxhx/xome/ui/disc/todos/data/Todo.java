package com.xxhx.xome.ui.disc.todos.data;

import java.util.Date;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxhx on 2018/1/10.
 */

@Entity
public class Todo {
    @Id
    private Long id;
    private boolean important;
    private boolean urgent;
    private boolean done;
    private String content;
    private Date addedDate;
    private Date dueDate;
    @Generated(hash = 576291624)
    public Todo(Long id, boolean important, boolean urgent, boolean done,
            String content, Date addedDate, Date dueDate) {
        this.id = id;
        this.important = important;
        this.urgent = urgent;
        this.done = done;
        this.content = content;
        this.addedDate = addedDate;
        this.dueDate = dueDate;
    }
    @Generated(hash = 1698043777)
    public Todo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getImportant() {
        return this.important;
    }
    public void setImportant(boolean important) {
        this.important = important;
    }
    public boolean getUrgent() {
        return this.urgent;
    }
    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getAddedDate() {
        return this.addedDate;
    }
    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
    public Date getDueDate() {
        return this.dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public boolean getDone() {
        return this.done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
}
