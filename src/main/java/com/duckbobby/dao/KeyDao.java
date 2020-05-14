package com.duckbobby.dao;

import com.duckbobby.model.Key;

import java.util.List;

/**
 * KeyDao
 * Created by witsir on 2017/12/21.
 */
public interface KeyDao {
    /**
     * 获取所有的关键词
     */
    List<Key> getKeyList();

    /**
     * 通过value 获取关键词ID
     */
    Key findKeyByValue(String keyValue);

    /**
     * 创建关键字
     */
    void createKey(Key key);
}
