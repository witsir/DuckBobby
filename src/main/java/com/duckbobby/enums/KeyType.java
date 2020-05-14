package com.duckbobby.enums;

/**
 * redis关键字枚举
 * Created by witsir on 2020/04/04.
 */
public enum KeyType {
    /**
     * 获取类别
     */
    GET_CATEGORY_LIST("getCategoryList"),GET_COUNT_LIST("getCountList");

    private String value;

    KeyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
