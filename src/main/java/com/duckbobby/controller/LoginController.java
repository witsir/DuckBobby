package com.duckbobby.controller;

import com.duckbobby.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录controller
 * Created by duckbobby on 2017/11/28.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/loginUser")
    @ResponseBody
    public String loginUser(HttpServletRequest request, String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            boolean login = userService.login(username, password);
            if (login) {
                request.getSession().setAttribute("user", username);
                return "success";
            }
        }
        return "fail";
    }
}
