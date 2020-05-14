package com.duckbobby.controller;

import com.duckbobby.model.Category;
import com.duckbobby.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 其他逻辑controller
 * Created by witsir on 2017/12/19.
 */
@Controller
public class OtherController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 404页面
     */
    @RequestMapping("/404")
    public String to404(Model model) {
        //获取类别
        List<Category> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "homepage/404";
    }

}
