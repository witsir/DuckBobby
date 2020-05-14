package com.duckbobby.model;

/**
 * Created by witsir on 2017/12/15.
 */
public class UeditorProperties {
    private String config = "{}";

    private String accessKey;

    private String secretKey;

    //七牛机房  华东：zone0 华北：zone1 华南：zone2 北美：zoneNa0
    private String zone = "autoZone";

    private String bucket;

    private String baseUrl;

    private String uploadDirPrefix = "ueditor/file/";

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUploadDirPrefix() {
        return uploadDirPrefix;
    }

    public void setUploadDirPrefix(String uploadDirPrefix) {
        this.uploadDirPrefix = uploadDirPrefix;
    }
}
