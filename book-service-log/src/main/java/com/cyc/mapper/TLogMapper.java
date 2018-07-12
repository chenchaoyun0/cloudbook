package com.cyc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cyc.common.po.TLog;
import com.cyc.common.vo.TodayCountResp;

import tk.mybatis.mapper.common.Mapper;

public interface TLogMapper extends Mapper<TLog> {
  TLog selectByUserIp(@Param("userIp")String userIp);

  Long selectLogSumCount();

  List<TLog> selectLogPages(@Param("tLog") TLog tLog);

  List<TLog> selectLogPagesForIp(@Param("userIp") String userIp);

  TodayCountResp todayCount(@Param("todayBegin") String todayBegin, @Param("todayEnd") String todayEnd);

  long totalPathCount(@Param("path") String path);
  
}