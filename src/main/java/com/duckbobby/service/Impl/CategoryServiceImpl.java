package com.duckbobby.service.Impl;

import com.duckbobby.dao.CategoryDao;
import com.duckbobby.enums.KeyType;
import com.duckbobby.model.Category;
import com.duckbobby.service.CategoryService;
import com.duckbobby.utils.myRedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryService实现类
 * Created by witsir on 2017/12/14.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private myRedisClient myRedisClient;

    @Override
    @Cacheable(value = "category")
    public List<Category> getCategoryList() {
        List<Category> getCategoryList = null;
        try {
            getCategoryList = myRedisClient.getList(KeyType.GET_CATEGORY_LIST.getValue());
            logger.info("CategoryService实现类"+getCategoryList);
            if (null == getCategoryList) {
                getCategoryList = categoryDao.getCategoryList();
                myRedisClient.setList(KeyType.GET_CATEGORY_LIST.getValue(), getCategoryList);
            }
        } catch (Exception e) {
            logger.error("get Category List error：", e);
        }
        return getCategoryList;
    }
}
