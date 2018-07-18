package com.cyc.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.TodayCountResp;

public interface ILogService {
  
  public int insertSelective(TLog tLog);

  TLog selectByUserIp(String userIp);
  
  int insert(TLog tLog);

  int updateByPrimaryKey(TLog record);

  Long selectLogSumCount();

  TodayCountResp todayCount();

  PagedResult<TLog> selectLogPages(TLog tLog, Integer pageNo, Integer pageSize);

  PagedResult<TLog> selectLogPagesForIp(String userIp, Integer pageNo, Integer pageSize);
  
  long totalPathCount(String path);
  
  public String visitors();
  
  public int saveBlackLisEntity(BlackLisEntity blackLisEntity);

}
