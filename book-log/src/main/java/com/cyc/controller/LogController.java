package com.cyc.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.base.BookResponse;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeVo;
import com.cyc.common.vo.TodayCountVo;
import com.cyc.context.FaceAppContextUtils;
import com.cyc.service.ILogService;

@RestController
public class LogController {

  private static final Logger log = LoggerFactory.getLogger(LogController.class);
  @Autowired
  private ILogService logService;

  @Value("${server.port}")
  String port;

  @RequestMapping("/info")
  public String home() {
    return "测试 book-log" + ",port:" + port;
  }

  @RequestMapping(value ="/indexHome", method = RequestMethod.GET)
  public IndexHomeVo indexHome(Integer pageNo, Integer pageSize) {
    IndexHomeVo indexHomeVo = new IndexHomeVo();
    try {
      HttpServletRequest request = FaceAppContextUtils.getCurrentRequest();
      TLog tLog = new TLog();
      PagedResult<TLog> pages = logService.selectLogPages(tLog, pageNo, pageSize);
      Long totalcount = logService.selectLogSumCount();
      TodayCountVo todayCount = logService.todayCount();
      String url = request.getRequestURI();
      pages.setUrl(url);
      indexHomeVo.setPages(pages);
      indexHomeVo.setTodayCount(todayCount);
      indexHomeVo.setTotalcount(totalcount);
      String resp = JSONObject.toJSONString(indexHomeVo);
      log.info(">>>>>>>>>indexHome resp:{}", resp);
    } catch (Exception e) {
      log.error("异常:{}", e);
      indexHomeVo.setErrorMsg(e.getMessage());
      indexHomeVo.setErrorCode(BookResponse.ERROR_CODE);
    }
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
  public String indexHomeForIp(String userIp, Model model, HttpServletRequest request, ModelAndView modelAndView,
    Integer pageNo, Integer pageSize) {
    // return "forward:/book/selectBookPages";
    PagedResult<TLog> pages = logService.selectLogPagesForIp(userIp, pageNo, pageSize);
    Long totalcount = logService.selectLogSumCount();
    String url = request.getRequestURI();
    pages.setStrWhere("userIp=" + userIp);
    pages.setUrl(url);
    model.addAttribute("pages", pages);
    model.addAttribute("totalcount", totalcount);
    return "ipLog";
  }


}
