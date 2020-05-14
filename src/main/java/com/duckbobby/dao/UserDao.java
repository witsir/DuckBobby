package com.duckbobby.dao;


import com.duckbobby.model.User;
import org.springframework.stereotype.Component;

/**
 * 用户dao
 * Created by witsir on 2016/12/19.
 */
@Component
public interface UserDao {
    /**
     * 登录
     */
    User login(User user);
}
