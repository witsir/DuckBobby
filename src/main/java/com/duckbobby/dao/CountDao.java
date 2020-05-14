package com.duckbobby.dao;

import com.duckbobby.model.Count;
import com.duckbobby.model.IPCount;

import java.util.List;

/**
 * Created by witsir on 2017/12/22.
 */
public interface CountDao {

    /**
     * 获取所有的点击量
     */
    List<Count> getCountList();

    /**
     * 通过articleId更新点击量
     *
     * @param articleId
     */
    void updateClickByArticleId(int articleId);

    /**
     * 根据articleId更新评论
     *
     * @param articleId
     */
    void updateCommentByArticleId(int articleId);

    /**
     * 更新所有的点击量
     */
    void updateAllClick(List<Count> list);

    /**
     * 更新所有的评论量
     */
    void updateAllComment(List<Count> list);

    /**
     * 增加文章时新增一个评论数据
     * @param articleId
     */
    void addCountByArticleId(int articleId);
}
