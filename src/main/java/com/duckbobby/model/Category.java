package com.duckbobby.model;

import java.io.Serializable;

/**
 * 类别
 * Created by witsir on 2017/12/14.
 */
public class Category implements Serializable{
    //ID
    private int id;
    //中文名
    private String chiName;

    public Category() {
    }

    public Category(int id, String chiName) {
        this.id = id;
        this.chiName = chiName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChiName() {
        return chiName;
    }

    public void setChiName(String chiName) {
        this.chiName = chiName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", chiName='" + chiName + '\'' +
                '}';
    }
}
