package com.cyc.controler.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * 
 * @author chenchaoyun
 * @date 2018/04/13
 */
public class FaceAppContextUtils {
  /**
   * 获取当前线程前端请求
   * 
   * @return
   */
  public static HttpServletRequest getCurrentRequest() {
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes sra = (ServletRequestAttributes)ra;
    if (sra == null)
      return null;

    return sra.getRequest();
  }

}
