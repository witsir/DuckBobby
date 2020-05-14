package com.duckbobby.service;

/**
 * UserService
 * Created by duckbobby on 2017/11/28.
 */
public interface UserService {
    /**
     * 用户登录情况
     *
     * @param username 用户名
     * @param password 密码
     */
    boolean login(String username, String password);
}
