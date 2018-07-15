package com.cyc.bookweb.aop;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cyc.bookweb.context.BlackListThreadLoacal;
import com.cyc.bookweb.feignclient.IBookLogClient;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.FaceAppContextUtils;
import com.cyc.common.utils.apaddress.IPUtils;
import com.cyc.common.utils.time.DateConvertUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.RenderingEngine;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Order(1)
@Slf4j
public class BookServiceControllerAop {

  @Autowired
  private IBookLogClient bookLogService;
  /**
   * 指定切方法
   */
  @Pointcut("execution(* com.cyc.bookweb.contrller..*.*(..))")
  public void access() {}

  @Before("access()")
  public void deBefore(JoinPoint joinPoint) {
    log.info("@Before...");
  }

  @AfterReturning(returning = "ret", pointcut = "access()")
  public void doAfterReturning(Object ret) {
    // 处理完请求，返回内容
    log.info("@AfterReturning...");
    log.info("方法的返回值 : {}", JSONObject.toJSONString(ret));
  }

  // 后置异常通知
  @AfterThrowing("access()")
  public void throwss(JoinPoint jp) {
    log.info("@AfterThrowing...");
    log.info("方法异常时执行.....");
  }

  // 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
  @After("access()")
  public void after(JoinPoint jp) {
    log.info("@After...");
    log.info("方法最后执行.....");
  }

  // 环绕通知,环绕增强，相当于MethodInterceptor
  @Around("access()")
  public Object arround(ProceedingJoinPoint joinPoint) {
    log.info("@Around...");
    Object o=null;
    try {
      long start = System.currentTimeMillis();
      log.info("方法环绕start...");
      o = joinPoint.proceed();
      log.info("方法环绕proceed end...结果:{}", JSONObject.toJSONString(o));
      // 保存日志
      long end = System.currentTimeMillis();
      long actionTime = end - start;
      log.info("++++++方法执行时间为:" + (actionTime));
      
      HttpServletRequest request = FaceAppContextUtils.getCurrentRequest();
      // 记录下请求内容
      log.info("请求URL : {}", request.getRequestURL().toString());
      log.info("请求方式HTTP_METHOD : {}", request.getMethod());
      String ip = IPUtils.getIpAddr(request);
      log.info("请求IP : {}", ip);
      String action = joinPoint.getSignature().getName();
      String module = joinPoint.getTarget().getClass().getSimpleName();
      log.info("请求CLASS_METHOD : {}",
        joinPoint.getSignature().getDeclaringTypeName() + "." + action);
      log.info("请求参数ARGS : {}", Arrays.toString(joinPoint.getArgs()));
      
      /**
       * 爬虫黑名单不记录日志
       */
      boolean b = BlackListThreadLoacal.getFlagBlackIp();
      if(b){
        log.info("++++++爬虫黑名单不记录日志,ip:{}",ip);
        return o;
      }
      
      
      /**
       * 构造参数
       */
      TLog tLog = new TLog();
      tLog.setUserIp(ip);
      tLog.setAction(action);
      tLog.setActionTime(actionTime);
      tLog.setCount(1l);
      tLog.setModule(module);
      tLog.setOperTime(DateConvertUtils.format(new Date(), DateConvertUtils.DATE_TIME_SSSS));
      String userName = "游客用户";
      tLog.setUserName(userName);
      String userNickName = "未设置";
      tLog.setUserNickName(userNickName);
      
      /**
       * 保存用户浏览器信息
       */
      String agentStr = request.getHeader("user-agent");
      log.info("用户浏览器信息agentStr:{}",agentStr);
      UserAgent agent = UserAgent.parseUserAgentString(agentStr);
      // 浏览器
      Browser browser = agent.getBrowser();
      // 浏览器版本
      Version version = agent.getBrowserVersion();
      // 系统
      OperatingSystem os = agent.getOperatingSystem();
      /**
       * 保存字段
       */
      // 浏览器类型
      BrowserType browserType = browser.getBrowserType();
      // 浏览器名称和版本
      String browserAndVersion = String.format("%s-%s", browser.getGroup().getName(), version.getVersion());
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
      tLog.setBrowserAndVersion(browserAndVersion);
      tLog.setBrowserType(browserType.name());
      tLog.setManufacturer(manufacturer.name());
      tLog.setRenderingEngine(renderingEngine.name());
      tLog.setSysName(sysName);
      tLog.setOperatingSystem(operatingSystem.name());
      tLog.setSysManufacturer(sysManufacturer.name());
      tLog.setDeviceType(deviceType.name());
      
      int i = bookLogService.saveLog(tLog);
      log.info("切面保存日志 resp:{}",i);
    } catch (Throwable e) {
      log.error("异常:{}", e);
    }
    return o;
  }
}
