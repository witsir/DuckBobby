package com.duckbobby.service.Impl;

import com.duckbobby.dao.CountDao;
import com.duckbobby.enums.KeyType;
import com.duckbobby.model.Count;
import com.duckbobby.service.CountService;
import com.duckbobby.utils.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 点击量和评论量service
 * Created by witsir on 2017/12/22.
 */
@Service
public class CountServiceImpl implements CountService {

    private static final Logger logger = LoggerFactory.getLogger(CountServiceImpl.class);

    @Autowired
    private CountDao countDao;

    @Autowired
    private RedisClient redisClient;

    @Override
    public List<Count> getCountList() {
        //先判断redis缓存是否存在
        List<Count> list = redisClient.getList(KeyType.GET_COUNT_LIST.getValue());
        if (null != list && list.size() > 0) {
            return list;
        } else {
            list = countDao.getCountList();
            try {
                redisClient.setList(KeyType.GET_COUNT_LIST.getValue(), list);
            } catch (Exception e) {
                logger.error("redis缓存点击量list失败：", e);
            }
            return list;
        }
    }

    @Override
    public void updateClickByArticleId(int articleId) {
        List<Count> list = redisClient.getList(KeyType.GET_COUNT_LIST.getValue());
        if (null != list && list.size() > 0) {
            for (Count count : list) {
                if (count.getArticleId() == articleId) {
                    count.setClick(count.getClick() + 1);
                    break;
                }
            }
            try {
                redisClient.setList(KeyType.GET_COUNT_LIST.getValue(), list);
            } catch (Exception e) {
                logger.error("更新redis点击量缓存list失败：", e);
                countDao.updateClickByArticleId(articleId);
            }
        } else {
            countDao.updateClickByArticleId(articleId);
        }
    }

    @Override
    public void updateCommentByArticleId(int articleId) {
        List<Count> list = redisClient.getList(KeyType.GET_COUNT_LIST.getValue());
        logger.info("查看有没有articleId数据：" + articleId);
        if (null != list && list.size() > 0) {
            for (Count count : list) {
                if (count.getArticleId() == articleId) {
                    count.setComment(count.getComment() + 1);
                    break;
                }
            }
            try {
                redisClient.setList(KeyType.GET_COUNT_LIST.getValue(), list);
            } catch (Exception e) {
                logger.error("更新redis评论量缓存list失败：", e);
                countDao.updateCommentByArticleId(articleId);
            }
        } else {
            countDao.updateCommentByArticleId(articleId);
        }
    }
}
