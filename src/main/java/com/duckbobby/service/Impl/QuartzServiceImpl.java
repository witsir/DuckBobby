package com.duckbobby.service.Impl;

import com.duckbobby.dao.CountDao;
import com.duckbobby.enums.KeyType;
import com.duckbobby.model.Count;
import com.duckbobby.service.QuartzService;
import com.duckbobby.utils.myRedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时器任务serviceImpl
 * Created by witsir on 2020/04/04.
 */
@Component
public class QuartzServiceImpl implements QuartzService {

    private static final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @Autowired
    private myRedisClient myRedisClient;

    @Autowired
    private CountDao countDao;

    /**
     * 每10秒钟时更新点击量
     */
    @Override
    @Scheduled(cron = "0 15 3 1/1 * ? ")
    public void timeToSaveClick() {
        logger.info("开启保存点击量定时器：" + LocalDateTime.now());
        List<Count> list = myRedisClient.getList(KeyType.GET_COUNT_LIST.getValue());
        logger.info("Quartz定时器 + redis缓存 getcountlsit 数据：" + list);
        if (null != list && list.size() > 0) {
            countDao.updateAllClick(list);
        } else {
            logger.info("Quartz定时器 + redis缓存获取点击量失败：" + LocalDateTime.now());
        }
    }

    /**
     * 每天凌晨3点15分定时更新评论数
     */
    @Override
    @Scheduled(cron = "0 15 3 1/1 * ? ")
    public void timeToSaveComment() {
        logger.info("开启保存评论量定时器：" + LocalDateTime.now());
        List<Count> list = myRedisClient.getList(KeyType.GET_COUNT_LIST.getValue());
        logger.info("Quartz定时器 + redis缓存 getcountlsit 数据：" + list);
        if (null != list && list.size() > 0) {
            countDao.updateAllComment(list);
        } else {
            logger.info("Quartz定时器 + redis缓存获取评论量失败：" + LocalDateTime.now());
        }
    }
}
