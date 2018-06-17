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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cyc.bookweb.feignclient.IBookLogClient;
import com.cyc.common.po.TLog;
import com.cyc.common.utils.FaceAppContextUtils;
import com.cyc.common.utils.time.DateConvertUtils;

import cn.itcast.commons.CommonUtils;

@Component
@Aspect
@Order(1)
public class BookServiceControllerAop {

  private static final Logger log = LoggerFactory.getLogger(BookServiceControllerAop.class);
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
      String remoteAddr = request.getRemoteAddr();
      String userIp = FaceAppContextUtils.getCurrentRequestIp();
      log.info("请求IP : {}", remoteAddr);
      log.info("请求IPuserIp : {}", userIp);
      String action = joinPoint.getSignature().getName();
      String module = joinPoint.getTarget().getClass().getSimpleName();
      log.info("请求CLASS_METHOD : {}",
        joinPoint.getSignature().getDeclaringTypeName() + "." + action);
      log.info("请求参数ARGS : {}", Arrays.toString(joinPoint.getArgs()));
      /**
       * 构造参数
       */
      TLog tLog = new TLog();
      tLog.setUserIp(remoteAddr);
      tLog.setAction(action);
      tLog.setActionTime(actionTime);
      tLog.setCount(1l);
      tLog.setLogId(CommonUtils.uuid());
      tLog.setModule(module);
      tLog.setOperTime(DateConvertUtils.format(new Date(), DateConvertUtils.DATE_TIME_SSSS));
      String userName = "游客用户";
      tLog.setUserName(userName);
      String userNickName = "未设置";
      tLog.setUserNickName(userNickName);
      int i = bookLogService.saveLog(tLog);
      log.info("切面保存日志 resp:{}",i);
    } catch (Throwable e) {
      log.error("异常:{}", e);
    }
    return o;
  }
}
