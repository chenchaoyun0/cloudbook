package com.cyc.bookweb.contrller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cyc.common.utils.apaddress.IPUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Slf4j
public class TestController {

  @RequestMapping(value = "/getSessionId")
  public void getSessionId(HttpServletRequest request, HttpServletResponse response) {

    try {
      String ipAddr = IPUtils.getIpAddr(request);
      Object o = request.getSession().getAttribute("springboot");
      if (o == null) {
        o = "spring boot 牛逼了!!!由ip:"+ipAddr+"端口:" + request.getLocalPort() + "生成";
        request.getSession().setAttribute("springboot", o);
      }
      String result
        = "ip:" + ipAddr + ",端口:" + request.getLocalPort() + ",sessionId=" + request.getSession().getId() + ";session data:{" + o+"}";
      response.setCharacterEncoding("GBK");
      response.getWriter().write(result);
    } catch (Exception e) {
      log.error("visitors error:{}", e);
    }
  }

}
