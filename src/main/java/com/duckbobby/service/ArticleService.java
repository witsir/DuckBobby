package com.duckbobby.service;

import com.duckbobby.model.Article;
import com.duckbobby.model.Count;
import com.duckbobby.model.Good;

import java.util.List;

/**
 * Created by duckbobby on 2017/11/29.
 */
public interface ArticleService {

    boolean createArticle(Article article);

    /**
     * 查询最新的设置的num数量的文章列表
     */
    List<Article> getArticleList(int num, List<Count> countList);

    /**
     * 通过内容ID查询文章内容
     *
     * @param contentId 内容ID
     */
    Article findArticleByContentId(String contentId);

    /**
     * 查询类别下的文章
     *
     * @param categoryId 类别ID
     */
    List<Article> getArticleByCategoryId(int categoryId, int num,List<Count> countList);

    /**
     * 获取热门文章
     */
    List<Article> getTopArticleList(String type);

    /**
     * 获取文章总数
     */
    int getArticleSize();

    /**
     * 获取 categoryId 文章总数
     */
    int getArticleSizeByCategoryId(int categoryId);

    /**
     * 获取每日一句
     */
    Good findGood();

    /**
     * 根据关键字查询
     */
    List<Article> getArticleByKeyword(String keyword, int i,List<Count> countList);

    /**
     * 根据关键字查询文章总数
     */
    int getArticleSizeByKeyword(String keyword);

    /**
     * 获取热门文章
     */
    List<Article> getTop5ArticleList(String s, List<Count> countList);

    /**
     * 修改文章
     */
    void updateArticle(Article article);

    /**
     * 根据Id删除文章
     * */
    void deleteArticle(int contentId);
}
