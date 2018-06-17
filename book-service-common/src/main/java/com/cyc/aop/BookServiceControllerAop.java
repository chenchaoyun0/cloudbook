package com.cyc.aop;

import java.util.Arrays;

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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.utils.FaceAppContextUtils;

@Component
@Aspect
@Order(1)
public class BookServiceControllerAop {

  private static final Logger log = LoggerFactory.getLogger(BookServiceControllerAop.class);

  /**
   * 指定切方法
   */
  @Pointcut("execution(* com.cyc.controller..*.*(..))")
  public void access() {}

  @Before("access()")
  public void deBefore(JoinPoint joinPoint) {
    log.info("@Before...");
    HttpServletRequest request = FaceAppContextUtils.getCurrentRequest();
    // 记录下请求内容
    log.info("请求URL : {}", request.getRequestURL().toString());
    log.info("请求方式HTTP_METHOD : {}", request.getMethod());
    log.info("请求IP : {}", request.getRemoteAddr());
    log.info("请求CLASS_METHOD : {}",
      joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    log.info("请求参数ARGS : {}", Arrays.toString(joinPoint.getArgs()));
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
  public Object arround(ProceedingJoinPoint pjp) {
    log.info("@Around...");
    log.info("方法环绕start...");
    try {
      Object o = pjp.proceed();
      log.info("方法环绕proceed end...结果:{}",JSONObject.toJSONString(o));
      return o;
    } catch (Throwable e) {
      log.error("异常:{}", e);
      return null;
    }
  }
}
