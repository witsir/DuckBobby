package com.duckbobby.model;

/**
 * 每日一句
 * Created by witsir on 2017/12/20.
 */
public class Good {

    private String id;

    private String chiContent;

    private String engContent;

    private String date;

    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChiContent() {
        return chiContent;
    }

    public void setChiContent(String chiContent) {
        this.chiContent = chiContent;
    }

    public String getEngContent() {
        return engContent;
    }

    public void setEngContent(String engContent) {
        this.engContent = engContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
