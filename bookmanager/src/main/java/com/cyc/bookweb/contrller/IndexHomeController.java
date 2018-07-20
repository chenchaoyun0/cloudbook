package com.cyc.bookweb.contrller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.bookweb.context.BlackListThreadLoacal;
import com.cyc.bookweb.feignclient.IBookLogClient;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.LogUtil;
import com.cyc.common.utils.apaddress.IPUtils;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;
import com.cyc.common.vo.LookResumeReq;
import com.cyc.common.vo.LookResumeResp;
import com.cyc.common.vo.SelectBlackListResp;
import com.cyc.common.vo.TodayCountResp;
import com.cyc.common.vo.VisitorsResp;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexHomeController {

  @Autowired
  private IBookLogClient bookLogService;

  @RequestMapping(value = "/selectBlackList", method = RequestMethod.GET)
  public void selectBlackList(HttpServletResponse response) {

    try {
      SelectBlackListResp resp = bookLogService.selectBlackList();
      response.setCharacterEncoding("GBK");
      String jsonString = JSONObject.toJSONString(resp);
      String formatAsJSON = LogUtil.formatAsJSON(jsonString);
      response.getWriter().write(formatAsJSON);
    } catch (Exception e) {
      log.error("visitors error:{}", e);
    }

  }

  @RequestMapping(value = "/visitors", method = RequestMethod.GET)
  public void visitors(HttpServletResponse response) {

    try {
      VisitorsResp resp = bookLogService.visitors();
      response.setCharacterEncoding("GBK");
      String jsonString = JSONObject.toJSONString(resp);
      String formatAsJSON = LogUtil.formatAsJSON(jsonString);
      response.getWriter().write(formatAsJSON);
    } catch (Exception e) {
      log.error("visitors error:{}", e);
    }

  }

  @RequestMapping(value = "/lookResume", method = RequestMethod.POST)
  @CrossOrigin
  public @ResponseBody LookResumeResp lookResume(@RequestBody LookResumeReq req, HttpServletRequest request) {
    log.info("查看网站主页 req:{}", JSONObject.toJSONString(req));
    log.info("浏览器的正式名称:{}", req.getAppName());
    log.info("浏览器的版本号:{}", req.getAppVersion());
    log.info("返回用户浏览器是否启用了cookie:{}", req.getCookieEnabled());
    log.info("返回用户计算机的cpu的型号:{}", req.getCpuClass());
    log.info("浏览器正在运行的操作系统平台:{}", req.getPlatform());
    log.info("浏览器的产品名（IE没有）:{}", req.getProduct());
    log.info("浏览器正在运行的操作系统，其中可能有CPU的信息（IE没有）:{}", req.getOscpu());
    log.info("关于浏览器更多信息（IE没有）:{}", req.getProductSub());
    log.info("userAgent:{}", req.getUserAgent());
    log.info("返回一个UserProfile对象，它存储用户的个人信息（火狐没有）:{}", req.getUserProfile());
    String ipAddr = IPUtils.getIpAddr(request);
    log.info("用户ip地址:{}", ipAddr);

    LookResumeResp resp = new LookResumeResp();
    // resp
    IndexHomeResp indexHomeVo = bookLogService.indexHome(1, 1);
    PagedResult<TLog> pages = indexHomeVo.getPages();
    TodayCountResp todayCount = indexHomeVo.getTodayCount();
    Long totalcount = indexHomeVo.getTotalcount();
    long totalPathCount = bookLogService.totalPathCount("lookResume");
    //
    resp.setTodayCount(todayCount.getTodayCount());
    resp.setTodayVisitorCount(todayCount.getTodayVisitorCount());
    resp.setTotalcount(totalcount);
    resp.setResumeCount(totalPathCount);
    resp.setTotalVisitorCount(pages.getPages());

    boolean b = BlackListThreadLoacal.getFlagBlackIp();
    if (b) {
      return resp;
    }
    //
    log.info("查看网站主页 http://www.shopbop.ink/");
    resp.setMsg("操作成功");
    log.info("查看网站主页 resp:{}", JSONObject.toJSONString(resp));
    return resp;
  }

  @RequestMapping(value = "/indexHome", method = RequestMethod.GET)
  public String indexHome(Model model, HttpServletRequest request, ModelAndView modelAndView, Integer pageNo,
    Integer pageSize) {
    IndexHomeResp indexHomeVo = bookLogService.indexHome(pageNo, pageSize);
    PagedResult<TLog> pages = indexHomeVo.getPages();
    TodayCountResp todayCount = indexHomeVo.getTodayCount();
    Long totalcount = indexHomeVo.getTotalcount();
    SelectBlackListResp resp = bookLogService.selectBlackList();
    long totalBlackList=0;
    if (resp.success()) {
      PageInfo<BlackLisEntity> pageInfo = resp.getPageInfo();
      totalBlackList=pageInfo.getTotal();
    }
    //
    String url = request.getRequestURI();
    pages.setUrl(url);
    model.addAttribute("pages", pages);
    model.addAttribute("totalcount", totalcount);
    model.addAttribute("todayCount", todayCount);
    model.addAttribute("totalBlackList", totalBlackList);
    log.info(">>>>>>>>>pages getTotal:{}", JSONObject.toJSON(pages.getTotal()));
    return "ipLog";
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
    IndexHomeForIpResp indexHomeForIpVo = bookLogService.indexHomeForIp(userIp, pageNo, pageSize);

    PagedResult<TLog> pages = indexHomeForIpVo.getPages();
    Long totalcount = indexHomeForIpVo.getTotalcount();

    String url = request.getRequestURI();
    pages.setStrWhere("userIp=" + userIp);
    pages.setUrl(url);
    model.addAttribute("pages", pages);
    model.addAttribute("totalcount", totalcount);
    return "ipLog";
  }
}