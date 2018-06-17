package com.cyc.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.base.BookResponse;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.FaceAppContextUtils;
import com.cyc.common.utils.apaddress.AddressUtils;
import com.cyc.common.utils.apaddress.IPAddressData;
import com.cyc.common.utils.apaddress.IPAddressMap;
import com.cyc.common.utils.apaddress.IPAddressVo;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;
import com.cyc.common.vo.TodayCountResp;
import com.cyc.service.ILogService;

import cn.itcast.commons.CommonUtils;

@RestController
public class LogController {

  private static final Logger log = LoggerFactory.getLogger(LogController.class);
  @Autowired
  private ILogService logService;

  @Value("${server.port}")
  private String port;

  @RequestMapping("/info")
  public String home() {
    PagedResult<TLog> pages = logService.selectLogPages(new TLog(), null, null);
    String jsonString = JSONObject.toJSONString(pages);
    return "测试 book-log" + ",port:" + port+";测试响应:"+jsonString;
  }

  @RequestMapping(value = "/indexHome", method = RequestMethod.GET)
  public IndexHomeResp indexHome(Integer pageNo, Integer pageSize) {
    IndexHomeResp indexHomeVo = new IndexHomeResp();
    try {
      TLog tLog = new TLog();
      PagedResult<TLog> pages = logService.selectLogPages(tLog, pageNo, pageSize);
      Long totalcount = logService.selectLogSumCount();
      TodayCountResp todayCount = logService.todayCount();
      indexHomeVo.setPages(pages);
      indexHomeVo.setTodayCount(todayCount);
      indexHomeVo.setTotalcount(totalcount);
    } catch (Exception e) {
      log.error("异常:{}", e);
      indexHomeVo.setErrorMsg(e.getMessage());
      indexHomeVo.setErrorCode(BookResponse.ERROR_CODE);
    }
    log.info(">>>>>>>>>indexHome resp:{}", JSONObject.toJSONString(indexHomeVo));
    return indexHomeVo;
  }

  @RequestMapping("/")
  public String indexResume(Model model, HttpServletRequest request, ModelAndView modelAndView) {
    return "forward:/indexHome";
  }

  @RequestMapping("/resume")
  public String resume() {
    return "job/m2/resume";
  }

  @RequestMapping(value = "/indexHomeForIp", method = RequestMethod.GET)
  public IndexHomeForIpResp indexHomeForIp(String userIp, Integer pageNo, Integer pageSize) {
    IndexHomeForIpResp resp = new IndexHomeForIpResp();
    try {
      PagedResult<TLog> pages = logService.selectLogPagesForIp(userIp, pageNo, pageSize);
      Long totalcount = logService.selectLogSumCount();
      //
      resp.setPages(pages);
      resp.setTotalcount(totalcount);
    } catch (Exception e) {
      log.error("异常:{}", e);
      resp.setErrorMsg(e.getMessage());
      resp.setErrorCode(BookResponse.ERROR_CODE);
    }
    log.info(">>>>>>>>>indexHome 响应:{}", JSONObject.toJSONString(resp));
    return resp;
  }

  @RequestMapping(value = "/saveLog", method = RequestMethod.POST)
  public int saveLog(@RequestBody TLog tLog) {
    String userIp = FaceAppContextUtils.getCurrentRequestIp();
    int insert = 0;
    try {
      //
      log.info("查找访问来源是否存在日志...begin");
      TLog logDb = logService.selectByUserIp(userIp);
      log.info("查找访问来源是否存在日志...end");
      log.info("+++++保存日志 exit begin...logDb" + JSONObject.toJSONString(logDb));
      if (logDb == null) {
        //
        String userAddress = "";
        IPAddressVo ipAddressVo = AddressUtils.getIPAddressVo(userIp);
        if (ipAddressVo == null || !"0".equals(ipAddressVo.getCode())) {
          userAddress = "搜不到你,请尝试刷新";
        } else {
          IPAddressData data = ipAddressVo.getData();
          String area = data.getArea();
          String country = data.getCountry();
          String province = data.getRegion();
          String city = data.getCity();
          String isp = data.getIsp();
          userAddress = area + "," + province + "," + city + "," + country + "," + isp;
        }
        String ipxy = Arrays.toString(IPAddressMap.getIPXY(userIp));
        String userJWD = ipxy==null?"未知":ipxy;
        // new
        tLog.setUserJwd(userJWD);
        tLog.setUserAddress(userAddress);
        log.info("+++++保存日志 new begin...参数" + JSONObject.toJSONString(tLog));
        insert = logService.insert(tLog);
        log.info("+++++保存日志 new end...+++++insert:{}" + insert);
      } else {
        logDb.setLogId(CommonUtils.uuid());
        logDb.setUserIp(userIp);
        logDb.setOperTime(tLog.getOperTime());
        logDb.setModule(tLog.getModule());
        logDb.setAction(tLog.getAction());
        logDb.setActionTime(tLog.getActionTime());
        log.info("+++++保存日志 exit begin...参数" + JSONObject.toJSONString(logDb));
        insert = logService.insert(logDb);
        log.info("+++++保存日志 exit end...+++++insert:{}" + insert);
      }
    } catch (Exception e) {
      log.error("异常:{}", e);
    }
    return insert;
  }

}
