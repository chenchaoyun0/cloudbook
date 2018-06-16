package com.cyc.service;

import java.io.InputStream;
import java.util.Collection;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.cyc.common.po.GridfsImg;
import com.cyc.common.utils.pages.PagedResult;
import com.mongodb.gridfs.GridFSDBFile;

public interface IBaseMongoRepository<T> {

    public PagedResult<T> selectPage(int pageNo, int pageSize, Query query, Class<T> clazz, Order... orders);

    public PagedResult<T> selectPage(int pageNo, int pageSize, Query query, Class<T> clazz);
    
    public int save(T t);

    public void saveBatch(Collection<T> cols);
    
    T findOne(Query query, Class<T> clazz);
    
    T updateOne(T t);
    
    long count(Query query, Class<T> clazz);
    
    String saveImg(GridfsImg gridfsImg);

  GridFSDBFile getById(Object id);

  InputStream getInputStreamById(Object id);

  void delFile(Object id);
}
