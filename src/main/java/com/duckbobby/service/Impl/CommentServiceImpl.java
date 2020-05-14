package com.duckbobby.service.Impl;

import com.duckbobby.dao.CommentDao;
import com.duckbobby.model.Comment;
import com.duckbobby.service.CommentService;
import com.duckbobby.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentServiceImpl
 * Created by witsir on 2020/12/20.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public void createComment(Comment comment) {
        comment.setDate(DateUtil.getDate());
        commentDao.createComment(comment);
    }

    @Override
    public List<Comment> findCommentByArticleId(int id) {
        List<Comment> articleComments = commentDao.findCommentByArticleId(id);
        List<Comment> convert = convert(articleComments);
        return convert;
    }


    /**
     * 转换评论方法，通过PID来进行关联
     *
     * @param list 评论数据
     */
    private List<Comment> convert(List<Comment> list) {
        List<Comment> articleComments = new ArrayList<>();
        for (Comment comment : list) {
            if (comment.getPid() == 0) {
                List<Comment> comments = new ArrayList<>();
                for (Comment commentChlid : list) {
                    if (comment.getId() == commentChlid.getPid()) {
                    comments.add(commentChlid);
                    }
                }
                comment.setComments(comments);
                articleComments.add(comment);
            }
        }
        return articleComments;
    }
}
