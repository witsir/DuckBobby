package com.duckbobby.model;

import java.io.Serializable;

/**
 * Created by witsir on 2017/12/22.
 */
public class Count implements Serializable{

    private int id;

    private int click;

    private int comment;

    private int articleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "Count{" +
                "id=" + id +
                ", click=" + click +
                ", comment=" + comment +
                ", articleId=" + articleId +
                '}';
    }
}
