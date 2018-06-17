package com.cyc.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.pages.BeanUtil;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.utils.time.DateConvertUtils;
import com.cyc.common.vo.TodayCountResp;
import com.cyc.mapper.TLogMapper;
import com.cyc.service.ILogService;
import com.github.pagehelper.PageHelper;

@Service("logService")
public class LogServiceImpl implements ILogService {

  private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

  @Autowired
  private TLogMapper tLogMapper;

  @Override
  public int insert(TLog tLog) {
    try {
      return insertSelective(tLog);
    } catch (UserException e) {
      log.error("异常,{}", e);
      throw new UserException("操作失败");
    }
  }

  @Override
  public int updateByPrimaryKey(TLog record) {
    try {
      return tLogMapper.updateByPrimaryKeySelective(record);
    } catch (UserException e) {
      log.error("异常,{}", e);
      throw new UserException("操作失败");
    }
  }

  @Override
  public PagedResult<TLog> selectLogPages(TLog tLog, Integer pageNo, Integer pageSize) {
    /**
     * 默认是12条
     */
    pageNo = pageNo == null ? (Integer)1 : pageNo;
    pageSize = pageSize == null ? (Integer)8 : pageSize;
    PageHelper.startPage(pageNo, pageSize);// 告诉插件开始分页
    List<TLog> selectLogPages = tLogMapper.selectLogPages(tLog);
    PagedResult<TLog> logPagedResult = BeanUtil.toPagedResult(selectLogPages);

    logPagedResult.setPageNo(pageNo);
    logPagedResult.setPageSize(pageSize);

    return logPagedResult;
  }

  @Override
  public PagedResult<TLog> selectLogPagesForIp(String userIp, Integer pageNo, Integer pageSize) {
    /**
     * 默认是12条
     */
    pageNo = pageNo == null ? (Integer)1 : pageNo;
    pageSize = pageSize == null ? (Integer)20 : pageSize;
    PageHelper.startPage(pageNo, pageSize);// 告诉插件开始分页
    List<TLog> selectLogPages = tLogMapper.selectLogPagesForIp(userIp);
    PagedResult<TLog> logPagedResult = BeanUtil.toPagedResult(selectLogPages);

    logPagedResult.setPageNo(pageNo);
    logPagedResult.setPageSize(pageSize);

    return logPagedResult;
  }

  @Override
  public Long selectLogSumCount() {
    try {
      return tLogMapper.selectLogSumCount();
    } catch (Exception e) {
      log.error("selectLogSumCount error,{}", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public TodayCountResp todayCount() {
    try {
      String todayBegin = DateConvertUtils.format(new Date()) + " 00:00:00";
      String todayEnd = DateConvertUtils.format(new Date()) + " 23:59:59";
      TodayCountResp todayCount = tLogMapper.todayCount(todayBegin, todayEnd);
      log.info("查询当日访问统计结果,todayCount:{}", JSONObject.toJSONString(todayCount));
      return todayCount;
    } catch (Exception e) {
      log.error("todayCount 异常,{}", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public int insertSelective(TLog tLog) {
    try {
      return tLogMapper.insertSelective(tLog);
    } catch (UserException e) {
      log.error("异常,{}", e);
      throw new UserException("操作失败");
    }
  }

}
