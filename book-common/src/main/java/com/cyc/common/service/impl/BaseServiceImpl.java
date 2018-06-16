package com.cyc.common.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyc.common.service.IBaseService;

import tk.mybatis.mapper.common.BaseMapper;


public class BaseServiceImpl<T> implements IBaseService<T> {
  private static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return baseMapper.insertSelective(t);
    }

    @Override
    public T selectByPrimaryKey(String id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return baseMapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return baseMapper.updateByPrimaryKey(t);
    }

    @Override
    public List<T> selectList(T t) {
        return baseMapper.select(t);
    }

    @Override
    public int batchSaveImg(List<T> tList) {
        int i =0;
        for (T t : tList) {
            i = insertSelective(t) + 1;
        }
        return i;
    }

}
