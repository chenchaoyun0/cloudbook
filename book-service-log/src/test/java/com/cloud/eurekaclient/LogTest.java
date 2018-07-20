package com.cloud.eurekaclient;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.cyc.BookLogApplication;
import com.cyc.common.po.Student;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.TodayCountResp;
import com.cyc.common.vo.VisitorsResp;
import com.cyc.mapper.StudentMapper;
import com.cyc.service.ILogService;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes={BookLogApplication.class})
public class LogTest {
  private static final Logger logger = LoggerFactory.getLogger(LogTest.class);
  @Autowired
  private ILogService logService;

  @Autowired
  private StudentMapper studentMapper;

  @Test
  @Transactional
  public void saveStudent() {
    Student student = new Student();
    student.setName("test");
    student.setScore(new BigDecimal("10.98"));
    int insertSelective = studentMapper.insertSelective(student);
    logger.info("insertPo:{}", JSONObject.toJSONString(insertSelective));
    logger.info("student.getId:{}", JSONObject.toJSONString(student.getId()));
  }

  @Test
  public void testSelectLogPages() {
    TLog tLog = new TLog();
    PagedResult<TLog> pagedResult = logService.selectLogPages(tLog, null, null);
    logger.info("+++++:{}", JSONObject.toJSON(pagedResult.getDataList().get(0)));
    PagedResult<TLog> pagedResult2 = logService.selectLogPages(new TLog(), null, null);
  }

  @Test
  public void testSelectByPrimaryKey() {
    /**
     * { "action": "indexHome", "actionTime": 29, "count": 0, "logId": "A55D9D20B88D421D92FA96D986EC65D2", "module":
     * "IndexHomeController", "operTime": "2017-03-09 02:29:38:655", "userAddress": "未分配或者内网IP----", "userIp":
     * "127.0.0.1", "userJwd": "未知", "userName": "游客用户", "userNickName": "未设置" }
     */
    TLog tLog = new TLog();
    tLog.setAction("indexHome");
    tLog.setActionTime(29l);
    tLog.setCount(0l);
    tLog.setModule("IndexHomeController");
    tLog.setUserAgent("setUserAgent");
    tLog.setUserJwd("未知");
    tLog.setOperTime("2017-03-09 02:29:38:655");
    tLog.setUserAddress("未分配或者内网IP----");
    tLog.setUserIp("127.0.0.1");
    tLog.setUserName("游客用户");
    tLog.setUserNickName("游客用户");
    logger.info("insertSelective~:{}" + JSONObject.toJSONString(tLog));
    int i = logService.insertSelective(tLog);
    logger.info("insertSelective~:{}" + i);
  }

  @Test
  public void testSelectLogSumCount() {
    Long selectLogSumCount = logService.selectLogSumCount();
    logger.info("+++++:{}", JSONObject.toJSON(selectLogSumCount));
  }

  @Test
  public void testSelectLogByIp() {
    PagedResult<TLog> pagedResult = logService.selectLogPagesForIp("127.0.0.1", null, null);
    logger.info("+++++:{}", JSONObject.toJSON(pagedResult));
  }

  @Test
  public void testTodayCount() {
    TodayCountResp todayCount = logService.todayCount();
    logger.info("+++++:{}", JSONObject.toJSON(todayCount));
  }
  @Test
  public void testVisitors() {
    VisitorsResp visitorsResp = logService.visitors();
    logger.info("+++++visitors:{}", JSONObject.toJSON(visitorsResp));
  }

}
