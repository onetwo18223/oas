package com.example.oas.api.service.impl;

import com.example.oas.api.db.dao.TbAmectTypeDao;
import com.example.oas.api.db.pojo.TbAmectType;
import com.example.oas.api.service.AmectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AmectTypeServiceImpl implements AmectTypeService {
    @Autowired
    private TbAmectTypeDao amectTypeDao;

    @Override
    public ArrayList<TbAmectType> searchAllAmectType() {
        ArrayList<TbAmectType> list = amectTypeDao.searchAllAmectType();
        return list;
    }
}