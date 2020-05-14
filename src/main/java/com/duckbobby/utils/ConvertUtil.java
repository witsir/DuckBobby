package com.duckbobby.utils;

import com.duckbobby.model.Article;
import com.duckbobby.model.Count;

import java.util.List;

/**
 * 转换工具类
 * Created by witsir on 2017/12/22.
 */
public class ConvertUtil {

    /**
     * 点击量列表添加到文章列表中
     *
     * @param articleList 文章列表
     * @param countList   点击量列表
     */
    public static void addClickConvert(List<Article> articleList, List<Count> countList) {
        articleList.forEach(article -> countList.forEach(count -> {
            if (article.getId() == count.getArticleId()) {
                article.setClick(count.getClick());
                article.setComment(count.getComment());
            }
        }));
    }
}
