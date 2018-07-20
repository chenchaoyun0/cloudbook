package com.cyc.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.base.ErrorCode;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.LogUtil;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.pages.BeanUtil;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.utils.time.DateConvertUtils;
import com.cyc.common.vo.SelectBlackListResp;
import com.cyc.common.vo.TodayCountResp;
import com.cyc.common.vo.VisitorsResp;
import com.cyc.mapper.BlackListMapper;
import com.cyc.mapper.TLogMapper;
import com.cyc.service.ILogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service("logService")
@Slf4j
public class LogServiceImpl implements ILogService {

  @Autowired
  private BlackListMapper blackListMapper;

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

  @Override
  public TLog selectByUserIp(String userIp) {
    try {
      return tLogMapper.selectByUserIp(userIp);
    } catch (UserException e) {
      log.error("异常,{}", e);
      throw new UserException("操作失败");
    }
  }

  @Override
  public long totalPathCount(String path) {
    try {

      return tLogMapper.totalPathCount(path);
    } catch (Exception e) {
      log.error("todayCount 异常,{}", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public VisitorsResp visitors() {
    VisitorsResp resp = new VisitorsResp();
    try {
      int pageNo = 1;
      int pageSize = 20;
      PageHelper.startPage(pageNo, pageSize);
      Example example = new Example(TLog.class);
      example.createCriteria().andEqualTo("action", "lookResume");
      example.orderBy("id").desc();
      List<TLog> list = tLogMapper.selectByExample(example);
      //
      PageInfo<TLog> pageInfo = new PageInfo<>(list);
      String jsonString = JSONObject.toJSONString(pageInfo);
      String formatAsJSON = LogUtil.formatAsJSON(jsonString);
      resp.setPageInfo(pageInfo);
      return resp;
    } catch (Exception e) {
      log.error("异常:{}", e);
      resp.setCode(ErrorCode.ERROR_CODE);
      resp.setMsg(e.getMessage());
      return resp;
    }
  }

  @Override
  public int saveBlackLisEntity(BlackLisEntity blackLisEntity) {
    try {
      return blackListMapper.insert(blackLisEntity);
    } catch (Exception e) {
      log.error("异常:{}", e);
    }
    return 0;
  }

  @Override
  public SelectBlackListResp selectBlackList() {
    SelectBlackListResp resp = new SelectBlackListResp();
    try {
      int pageNo = 1;
      int pageSize = 20;
      PageHelper.startPage(pageNo, pageSize);
      List<BlackLisEntity> list = blackListMapper.selectAll();
      //
      PageInfo<BlackLisEntity> pageInfo = new PageInfo<>(list);
      resp.setPageInfo(pageInfo);
      return resp;
    } catch (Exception e) {
      log.error("异常:{}", e);
      resp.setCode(ErrorCode.ERROR_CODE);
      resp.setMsg(e.getMessage());
      return resp;
    }
  }
}
