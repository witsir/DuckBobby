package com.duckbobby.service;

import com.duckbobby.model.Category;

import java.util.List;

/**
 * CategoryService 接口
 * Created by witsir on 2017/12/14.
 */
public interface CategoryService {

    /**
     * 查找所有的类别
     */
    List<Category> getCategoryList();
}
