package com.duckbobby.dao;

import com.duckbobby.model.Article;
import com.duckbobby.model.Content;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文章和内容DAO
 * Created by duckbobby on 2017/11/29.
 */
@Component
public interface ArticleDao {

    /**
     * 添加文章
     *
     * @param article 文章
     */
    int createArticle(Article article);

    /**
     * 添加内容
     *
     * @param content 内容
     */
    int createContent(Content content);

    /**
     * 更新内容
     *
     * @param content 内容
     */
    int updateContent(Content content);

    /**
     * 获取文章
     */
    List<Article> getArticleList(int mun);

    /**
     * 查询文章通过内容ID
     *
     * @param contentId 内容ID
     */
    Article findArticleByContentId(String contentId);

    /**
     * 查询内容通过内容ID
     *
     * @param contentId 内容ID
     */
    Content findContentByContentId(String contentId);

    /**
     * 查询类别下的文章
     *
     * @param id 类别ID
     */
    List<Article> getArticleByCategoryId(@Param("id") int id, @Param("num") int num);

    /**
     * 获取热门文章
     */
    List<Article> getTopArticleList(String type);

    /**
     * 获取文章总数
     */
    int getArticleSize();

    /**
     * 获取categoryId文章总数
     */
    int getArticleSizeByCategoryId(int categoryId);

    /**
     * 根据Keyword查找文章列表
     * @param keyword 关键字
     * @param i
     * @return
     */
    List<Article> getArticleByKeyword(@Param("keyword") String keyword, @Param("num") int i);

    int getArticleSizeByKeyword(String keyword);
    /*
     *更新文章
     */
    void updateArticle(Article article);

    /**
     * 根据contentId删除文章
     * @param contentId
     */
    void deleteArticle(int contentId);
}
