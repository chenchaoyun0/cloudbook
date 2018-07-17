package com.cyc.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.base.ErrorCode;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.LogUtil;
import com.cyc.common.utils.apaddress.AddressUtils;
import com.cyc.common.utils.apaddress.IPAddressData;
import com.cyc.common.utils.apaddress.IPAddressMap;
import com.cyc.common.utils.apaddress.IPAddressVo;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;
import com.cyc.common.vo.TodayCountResp;
import com.cyc.mapper.BlackListMapper;
import com.cyc.mapper.TLogMapper;
import com.cyc.service.ILogService;

import tk.mybatis.mapper.entity.Example;

@RestController
public class LogController {

  private static final Logger log = LoggerFactory.getLogger(LogController.class);
  @Autowired
  private ILogService logService;

  @Value("${server.port}")
  private String port;

  @Autowired
  private BlackListMapper blackListMapper;

  @Autowired
  private TLogMapper tLogMapper;

  @RequestMapping(value = "/selectBlackLisEntityByIp", method = RequestMethod.GET)
  public BlackLisEntity selectBlackLisEntityByIp(@RequestParam(value = "ip") String ip) {
    BlackLisEntity blackLisEntity = null;
    try {
      Example example = new Example(BlackLisEntity.class);
      example.createCriteria().andEqualTo("ip", ip);
      blackLisEntity = blackListMapper.selectOneByExample(example);
      return blackLisEntity;
    } catch (Exception e) {
      log.error("异常:{}", e);
    }
    return blackLisEntity;
  }

  @RequestMapping(value = "/updateBlackLisEntitySelective", method = RequestMethod.POST)
  public int updateBlackLisEntitySelective(BlackLisEntity blackLisEntity) {
    try {
      Example exampleUpdate = new Example(BlackLisEntity.class);
      exampleUpdate.createCriteria().andEqualTo("id", blackLisEntity.getId());
      int updateByPrimaryKey = blackListMapper.updateByExampleSelective(blackLisEntity, exampleUpdate);
      return updateByPrimaryKey;
    } catch (Exception e) {
      log.error("异常:{}", e);
    }
    return 0;
  }

  @RequestMapping(value = "/saveBlackLisEntity", method = RequestMethod.POST)
  public int saveBlackLisEntity(BlackLisEntity blackLisEntity) {
    try {
      return blackListMapper.insert(blackLisEntity);
    } catch (Exception e) {
      log.error("异常:{}", e);
    }
    return 0;
  }

  @RequestMapping(value = "/visitors", method = RequestMethod.GET)
  public String visitors() {
    return logService.visitors();
  }

  @RequestMapping(value = "/totalPathCount", method = RequestMethod.GET)
  public long totalPathCount(@RequestParam(value = "path") String path) {
    try {
      return logService.totalPathCount(path);
    } catch (Exception e) {
      log.error("异常:{}", e);
    }
    return 0;
  }

  @RequestMapping("/info")
  public String home() {
    PagedResult<TLog> pages = logService.selectLogPages(new TLog(), null, null);
    String jsonString = JSONObject.toJSONString(pages);
    return "测试 book-log" + ",port:" + port + ";测试响应:" + jsonString;
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
      indexHomeVo.setMsg(e.getMessage());
      indexHomeVo.setCode(ErrorCode.ERROR_CODE);
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
      resp.setMsg(e.getMessage());
      resp.setCode(ErrorCode.ERROR_CODE);
    }
    log.info(">>>>>>>>>indexHome 响应:{}", JSONObject.toJSONString(resp));
    return resp;
  }

  @RequestMapping(value = "/saveLog", method = RequestMethod.POST)
  public int saveLog(@RequestBody TLog tLog) {
    String userIp = tLog.getUserIp();
    tLog.setUserIp(userIp);
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
          if (StringUtils.isBlank(area)) {
            userAddress = province + "," + city + "," + country + "," + isp;
          } else {
            userAddress = area + "," + province + "," + city + "," + country + "," + isp;
          }
        }
        String ipxy = Arrays.toString(IPAddressMap.getIPXY(userIp));
        String userJWD = ipxy == null || ipxy.equals("null") ? "未知" : ipxy;
        // new
        tLog.setUserJwd(userJWD);
        tLog.setUserAddress(userAddress);
        log.info("+++++保存日志 new begin...参数" + JSONObject.toJSONString(tLog));
        insert = logService.insert(tLog);
        log.info("+++++保存日志 new end...+++++insert:{}" + insert);
      } else {
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
