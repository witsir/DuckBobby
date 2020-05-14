package com.duckbobby.service.Impl;

import com.duckbobby.dao.UserDao;
import com.duckbobby.model.User;
import com.duckbobby.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户serviceImpl
 * Created by duckbobby on 2017/11/28.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public boolean login(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User login = userDao.login(user);
        return null != login;
    }
}
