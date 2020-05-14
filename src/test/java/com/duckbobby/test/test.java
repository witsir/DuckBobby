package com.duckbobby.test;

import com.duckbobby.enums.KeyType;
import com.duckbobby.model.Category;
import com.duckbobby.model.Count;
import com.duckbobby.utils.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Autowired
    RedisClient redisClient;

    @Test
    public void test() throws Exception {
        List<Count> list = redisClient.getList(KeyType.GET_COUNT_LIST.getValue());
        System.out.println(list);

    }
}