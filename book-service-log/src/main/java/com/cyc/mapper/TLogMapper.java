package com.cyc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cyc.common.po.TLog;
import com.cyc.common.po.TLogKey;
import com.cyc.common.vo.TodayCountResp;
public interface TLogMapper {
    int deleteByPrimaryKey(TLogKey key);

    int insert(TLog record);

    int insertSelective(TLog record);

    TLog selectByUserIp(String userIp);

    TLog selectByPrimaryKey(TLogKey key);

    int updateByPrimaryKeySelective(TLog record);

    int updateByPrimaryKey(TLog record);

    Long selectLogSumCount();

    List<TLog> selectLogPages(@Param("tLog") TLog tLog);

    List<TLog> selectLogPagesForIp(@Param("userIp") String userIp);
    
    TodayCountResp todayCount(@Param("todayBegin") String todayBegin,@Param("todayEnd") String todayEnd);
}