package com.duckbobby.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * redis客户端
 * Created by witsir on 2020/04/04.
 */

public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);


    private JedisPool jedisPool = new JedisPool();

    /**
     * 存储String
     *
     * @param key   key
     * @param value String
     * @throws Exception e
     */
    public void setString(String key, String value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } finally {
            //返还到连接池
            assert jedis != null;
            jedis.close();
        }
    }

    /**
     * 获取String
     *
     * @param key key
     * @return String
     * @throws Exception e
     */
    public String getString(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            //返还到连接池
            assert jedis != null;
            jedis.close();
        }
    }

    /**
     * 存储list
     *
     * @param key  key
     * @param list List
     * @param <T>  泛型
     * @throws Exception ex
     */
    public <T> void setList(String key, List<T> list) throws Exception {
        setObject(key, ObjectTranscoder.serialize(list));
    }

    /**
     * 获取list
     *
     * @param key key
     * @return List
     */
    public <T> List<T> getList(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] in = jedis.get(key.getBytes("UTF-8"));
//            byte[] in = jedis.get(key.getBytes("UTF-8"));
            List<T> list = (List<T>) ObjectTranscoder.deserialize(in);
            return list;
        } catch (UnsupportedEncodingException e) {
            logger.error("redis获取对象获取UTF-8编码失败", e);
            throw new RuntimeException(e);
        } finally {
            //返还到连接池
            assert jedis != null;
            jedis.close();
        }
    }

    /**
     * 存储对象
     *
     * @param key key
     * @param o   对象
     */
    private void setObject(String key, byte[] o) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key.getBytes("UTF-8"), o);
        } catch (UnsupportedEncodingException e) {
            logger.error("redis存储对象获取UTF-8编码失败", e);
        } finally {
            //返还到连接池
            assert jedis != null;
            jedis.close();
        }
    }

    /**
     * 存储map
     *
     * @param key key
     * @param map Map
     * @param <T> 泛型
     * @throws Exception Exception
     */
    public <T> void setMap(String key, Map<String, T> map) throws Exception {
        setObject(key, ObjectTranscoder.serialize(map));
    }

    /**
     * 获取map
     *
     * @param key key
     * @return Map
     */
    public <T> Map<String, T> getMap(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] in = jedis.get(key.getBytes("UTF-8"));
            Map<String, T> map = (Map<String, T>) ObjectTranscoder.deserialize(in);
            return map;
        } catch (Exception e) {
            logger.error("redis获取对象失败", e);
            throw new RuntimeException(e);
        } finally {
            //返还到连接池
            assert jedis != null;
            jedis.close();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
