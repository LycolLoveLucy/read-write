package com.application.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_table")
public class Account {
    public Account() {
    }

    public Account(String id, String name) {
        this.id = id;
        this.name = name;

    }


    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    @TableField(value = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
