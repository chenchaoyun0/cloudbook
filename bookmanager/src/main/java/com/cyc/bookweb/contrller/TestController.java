package com.cyc.bookweb.contrller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.utils.LogUtil;
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
        o = "spring boot redis session 测试,由 ip:" + ipAddr + "端口:" + request.getLocalPort() + "生成";
        request.getSession().setAttribute("springboot", o);
      }
      JSONObject json = new JSONObject();
      json.put("ipAddress", ipAddr);
      json.put("port", request.getLocalPort());
      json.put("sessionId", request.getSession().getId());
      json.put("sessionData", o);
      String formatAsJSON = LogUtil.formatAsJSON(json.toJSONString());
      response.setCharacterEncoding("GBK");
      response.getWriter().write(formatAsJSON);
    } catch (Exception e) {
      log.error("visitors error:{}", e);
    }
  }

}
