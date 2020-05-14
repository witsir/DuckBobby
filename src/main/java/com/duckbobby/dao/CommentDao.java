package com.duckbobby.dao;

import com.duckbobby.model.Comment;

import java.util.List;

/**
 * CommentDao
 * Created by witsir on 2017/12/20.
 */
public interface CommentDao {
    /**
     * 新增留言
     */
    void createComment(Comment comment);

    /**
     * 根据文章ID获取留言
     */
    List<Comment> findCommentByArticleId(int id);

}
