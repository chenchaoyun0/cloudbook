package com.cyc.bookweb.contrller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.cyc.common.base.ErrorCode;
import com.cyc.common.po.TLog;
import com.cyc.common.po.VisitorProfile;
import com.cyc.common.utils.BookManagerBeanUtils;
import com.cyc.common.utils.apaddress.AddressUtils;
import com.cyc.common.utils.apaddress.IPAddressData;
import com.cyc.common.utils.apaddress.IPAddressVo;
import com.cyc.common.utils.apaddress.IPUtils;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.utils.time.DateConvertUtils;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;
import com.cyc.common.vo.LookResumeReq;
import com.cyc.common.vo.LookResumeResp;
import com.cyc.common.vo.TodayCountResp;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.RenderingEngine;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexHomeController {

  @Autowired
  private IBookLogClient bookLogService;

  @RequestMapping(value = "/visitors", method = RequestMethod.GET)
  public void visitors(HttpServletResponse response) {

    try {
      String visitors = bookLogService.visitors();
      response.setCharacterEncoding("GBK");
      response.getWriter().write(visitors);
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

    /**
     * 保存
     */
    String userAddress = "";
    IPAddressVo ipAddressVo = AddressUtils.getIPAddressVo(ipAddr);
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
    log.info("通过ip解析用户地址:{}", userAddress);
    //
    log.info("查看网站主页 http://www.shopbop.ink/");
    try {

      /**
       * 保存用户浏览器信息
       */
      String agentStr = request.getHeader("user-agent");
      log.info("用户浏览器信息agentStr:{}", agentStr);
      UserAgent agent = UserAgent.parseUserAgentString(agentStr);
      // 浏览器
      Browser browser = agent.getBrowser() == null ? Browser.UNKNOWN : agent.getBrowser();
      // 浏览器版本
      Version version = agent.getBrowserVersion();
      // 系统
      OperatingSystem os = agent.getOperatingSystem() == null ? OperatingSystem.UNKNOWN : agent.getOperatingSystem();
      /**
       * 保存字段
       */
      // 浏览器类型
      BrowserType browserType = browser.getBrowserType();
      // 浏览器名称和版本
      String browserAndVersion
        = String.format("%s-%s", browser.getGroup().getName(), version == null ? "未知" : version.getVersion());
      // 浏览器厂商
      Manufacturer manufacturer = browser.getManufacturer();
      // 浏览器引擎
      RenderingEngine renderingEngine = browser.getRenderingEngine();
      // 系统名称
      String sysName = os.getName();
      // 产品系列
      OperatingSystem operatingSystem = os.getGroup();
      // 生成厂商
      Manufacturer sysManufacturer = os.getManufacturer();
      // 设备类型
      DeviceType deviceType = os.getDeviceType();
      // 浏览器信息
      
      
      int insert = bookLogService.saveLog(visitorProfile);
      log.info("保存用户信息:{}", insert);

    } catch (Exception e) {
      log.error("查看网站主页异常:{}", e);
      resp.setCode(ErrorCode.ERROR_CODE);
      resp.setMsg(e.getMessage());
    }
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
    //
    String url = request.getRequestURI();
    pages.setUrl(url);
    model.addAttribute("pages", pages);
    model.addAttribute("totalcount", totalcount);
    model.addAttribute("todayCount", todayCount);
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