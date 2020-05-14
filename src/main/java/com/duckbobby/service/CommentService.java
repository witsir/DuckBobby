package com.duckbobby.service;

import com.duckbobby.model.Comment;

import java.util.List;

/**
 * CommentService 接口
 * Created by witsir on 2017/12/20.
 */
public interface CommentService {

    /**
     * 添加留言
     */
    void createComment(Comment comment);

    /**
     * 获取文章留言
     */
    List<Comment> findCommentByArticleId(int id);

}
