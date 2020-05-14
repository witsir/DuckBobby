package com.duckbobby.service.Impl;

import com.duckbobby.dao.KeyDao;
import com.duckbobby.model.Key;
import com.duckbobby.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * KeyServiceImpl
 * Created by witsir on 2017/12/21.
 */
@Service
public class KeyServiceImpl implements KeyService {


    @Autowired
    private KeyDao keyDao;

    @Override
    public List<Key> getKeyList() {
        return keyDao.getKeyList();
    }
}

