package com.cyc.service.impl;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.cyc.common.po.GridfsImg;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.service.IBaseMongoRepository;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

@Repository("baseMongoRepository")
public class BaseMongoRepositoryImpl<T> implements IBaseMongoRepository<T> {

  private static final Logger logger = LoggerFactory.getLogger(BaseMongoRepositoryImpl.class);

  @Resource
  private MongoTemplate mongoTemplate;

  @Autowired
  private GridFsTemplate gridFsTemplate;
//  @Autowired
//  private GridFSBucket gridFSBucket;

  @Value("${gridfs.url}")
  private String gridfsUrl;

  @Override
  public PagedResult<T> selectPage(int pageNo, int pageSize, Query query, Class<T> clazz, Order... orders) {
    long listQueryStart = System.currentTimeMillis();
    // sort

    Sort sort = new Sort(orders);
    if (null != sort) {
      query.with(sort);
    }
    // query
    PagedResult<T> pagelist = selectPage(pageNo, pageSize, query, clazz);
    logger.info("selectPage {} cost {} ms ", clazz.getSimpleName(), (System.currentTimeMillis() - listQueryStart));
    return pagelist;
  }

  @Override
  public PagedResult<T> selectPage(int pageNO, int pageSize, Query query, Class<T> clazz) {
    // query
    long count = mongoTemplate.count(query, clazz);
    PagedResult<T> pageable = new PagedResult<T>();
    // 自己计算..
    // int pagenumber=(start/iDisplayLength)+1;
    // 开始页
    pageable.setPageNo(pageNO);
    // 每页条数
    pageable.setPageSize(pageSize);
    List<T> list = mongoTemplate.find(query.with(pageable), clazz);
    Page<T> pagelist = new PageImpl<T>(list, pageable, count);
    int numberOfElements = pagelist.getNumberOfElements();// 当前页条数
    int number = pagelist.getNumber();// 页码
    // int size = pagelist.getSize();// 分页要求条数
    long totalElements = pagelist.getTotalElements();// 总记录数
    int totalPages = pagelist.getTotalPages();// 总页数
    //
    pageable.setDataList(pagelist.getContent());
    pageable.setPageNo(number);
    pageable.setPageSize(numberOfElements);
    pageable.setTotal(totalElements);
    pageable.setPages(totalPages);
    return pageable;
  }

  @Override
  public int save(T t) {
    try {
      mongoTemplate.save(t);
    } catch (Exception e) {
      logger.error("mongoTemplate save error", e);
      return 0;
    }
    return 1;
  }

  @Override
  public void saveBatch(Collection<T> cols) {
    for (T t : cols) {
      save(t);
    }
  }

  @Override
  public T findOne(Query query, Class<T> clazz) {
    T findOne = mongoTemplate.findOne(query, clazz);
    return findOne;
  }

  @Override
  public T updateOne(T t) {
    // TODO Auto-generated method stub
    Query query = new Query();
    Update update = new Update();
    // 更新查询返回结果集的第一条
    mongoTemplate.updateFirst(query, update, t.getClass());
    return null;
  }

  @Override
  public long count(Query query, Class<T> clazz) {
    long count = mongoTemplate.count(query, clazz);
    return count;
  }

  @Override
  public String saveImg(GridfsImg gridfsImg) {
    String url = null;
    String fileName = gridfsImg.getFileName();
    InputStream in = gridfsImg.getIn();
    try {
      GridFSFile gridFSFile = gridFsTemplate.store(in, fileName);
      Object id = gridFSFile.getId().toString();
      // 占位符替换
      logger.info("store id:{}",id);
      url = MessageFormat.format(gridfsUrl, id);
    } catch (Exception e) {
      logger.error("saveImg error:{}", e);
    }
    return url;
  }

  @Override
  public GridFSDBFile getById(Object id) {
    Query query = new Query(GridFsCriteria.where("_id").is(id));
    GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(query);
    return gridFSDBFile;
  }

  @Override
  public InputStream getInputStreamById(Object id) {
    InputStream inputStream = null;
    try {
      GridFSDBFile gridFSDBFile = getById(id);
      //GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
      //GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
      inputStream = gridFSDBFile.getInputStream();
    } catch (Exception e) {
      logger.error("getInputStreamById error:{}", e);
    }
    return inputStream;
  }

  @Override
  public void delFile(Object id) {
    Query query = new Query(GridFsCriteria.where("_id").is(id));
    gridFsTemplate.delete(query);
  }

}
