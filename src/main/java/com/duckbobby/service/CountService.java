package com.duckbobby.service;

import com.duckbobby.model.Count;

import java.util.List;

/**CountService
 *
 * Created by witsir on 2017/12/22.
 */
public interface CountService {

    List<Count> getCountList();

    /**
     * 添加点击量
     */
    void updateClickByArticleId(int articleId);

    /**
     * 添加评论数
     */
    void updateCommentByArticleId(int articleId);

}
