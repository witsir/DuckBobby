package com.duckbobby.utils;

import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 七牛工具类
 * Created by witsir on 2017/12/15.
 */

public class QiNiuUtils {
        static String accessKey = "yHt5FarMck31aIcUvTrD7Bxit15WnExmgUKtkTRF";
        static String secretKey = "fpKBdxPpWpCKHWGd8a3VUsRY7tDCfQc8UF4qUuay";
        static String bucket = "duckbobby1";
    public static Map updataFile(InputStream inputStream, String name) {
        Map<String, String> map = new HashMap<>();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            //...生成上传凭证，然后准备上传
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            name = LocalDateTime.now() + "_" + name;
            Response response = uploadManager.put(inputStream, name, upToken, null, null);
            int statusCode = response.statusCode;
            if (statusCode == 200) {
                map.put("state", "SUCCESS");
                map.put("title", name);
                String[] toType = name.split("\\.");
                map.put("type", toType[toType.length-1]);
                map.put("url", name);
            } else {
                map.put("state", "FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
