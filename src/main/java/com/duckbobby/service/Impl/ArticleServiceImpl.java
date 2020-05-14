package com.duckbobby.service.Impl;

import com.duckbobby.dao.ArticleDao;
import com.duckbobby.dao.CountDao;
import com.duckbobby.dao.GoodDao;
import com.duckbobby.dao.KeyDao;
import com.duckbobby.model.*;
import com.duckbobby.service.ArticleService;
import com.duckbobby.utils.ConvertUtil;
import com.duckbobby.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by duckbobby on 2017/11/29.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private KeyDao keyDao;
    @Autowired
    private GoodDao goodDao;

    @Override
    public boolean createArticle(Article article) {
        Content content = new Content();
        content.setContent(article.getContent());
        articleDao.createContent(content);

        Key key = keyDao.findKeyByValue(article.getKeyValue());
        if (null == key) {
            Key key1 = new Key();
            key1.setValue(article.getKeyValue());
            keyDao.createKey(key1);
            //关键词ID
            article.setKeyID(key1.getId());
            //关键词
            article.setKeyValue(key1.getValue());
        } else {
            //关键词ID
            article.setKeyID(key.getId());
            //关键词
            article.setKeyValue(key.getValue());
        }
        //内容ID
        article.setContentId(content.getId());
        //创建日期
        article.setCreateDate(DateUtil.getDate());

        String[] cate = article.getCategoryId().split("_");
        //类别ID
        article.setCategoryId(cate[0]);
        //类别名称
        article.setCategoryValue(cate[1]);
        //创建文章
        articleDao.createArticle(article);
        //创建点击量表数据


        return true;
    }

    @Override
    @Cacheable(value = "newArticle")
    public List<Article> getArticleList(int num, List<Count> countList) {
        List<Article> articleList = articleDao.getArticleList(num * 5);
        ConvertUtil.addClickConvert(articleList, countList);
        return articleList;
    }

    @Override
    public Article findArticleByContentId(String contentId) {
        Article article = articleDao.findArticleByContentId(contentId);
        if (null == article) {
            logger.error("没找到文章：contentId为" + contentId);
            throw new RuntimeException("没找到文章：contentId为" + contentId);
        }
        Content content = articleDao.findContentByContentId(contentId);
        article.setContent(content.getContent());
        return article;
    }

    @Override
    public List<Article> getArticleByCategoryId(int categoryId, int num, List<Count> countList) {
        List<Article> articleByCategoryId = articleDao.getArticleByCategoryId(categoryId, num * 5);
        ConvertUtil.addClickConvert(articleByCategoryId, countList);
        return articleByCategoryId;
    }

    @Override
    @Cacheable(value = "topArticle")
    public List<Article> getTopArticleList(String type) {
        return articleDao.getTopArticleList(type);
    }

    @Override
    public int getArticleSize() {
        return articleDao.getArticleSize();
    }

    @Override
    public int getArticleSizeByCategoryId(int categoryId) {
        return articleDao.getArticleSizeByCategoryId(categoryId);
    }

    @Override
    @Cacheable(value = "good")
    public Good findGood() {
        Good good = goodDao.findGood();
//        good.setDate(DateUtil.getDate("yyyy年MM月dd日") + "   " + DateUtil.getWeek());
        return good;
    }

    @Override
    public List<Article> getArticleByKeyword(String keyword, int i, List<Count> countList) {
        List<Article> articleList = articleDao.getArticleByKeyword(keyword, i * 5);
        ConvertUtil.addClickConvert(articleList, countList);
        return articleList;
    }

    @Override
    public int getArticleSizeByKeyword(String keyword) {
        return articleDao.getArticleSizeByKeyword(keyword);
    }

    @Override
    public List<Article> getTop5ArticleList(String s, List<Count> countList) {
        List<Article> topArticleList = getTopArticleList(s);
        ConvertUtil.addClickConvert(topArticleList, countList);
        return topArticleList;
    }

    @Override
    public void updateArticle(Article article) {
        Content content = new Content();
        content.setId(article.getContentId());
        content.setContent(article.getContent());
        articleDao.updateContent(content);
        articleDao.updateArticle(article);
    }

    @Override
    public void deleteArticle(int contentId) {
        articleDao.deleteArticle(contentId);
    }
}
