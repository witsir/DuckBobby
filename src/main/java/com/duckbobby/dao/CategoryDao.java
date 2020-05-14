package com.duckbobby.dao;

import com.duckbobby.model.Category;

import java.util.List;

/**
 * CategoryDao类
 * Created by witsir on 2017/12/14.
 */
public interface CategoryDao {

    List<Category> getCategoryList();
}
