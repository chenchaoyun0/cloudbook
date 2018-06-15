package com.cyc.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookLogControler {
  @Value("${server.port}")
  String port;

  @RequestMapping("/hi")
  public String home(@RequestParam String name) {
    return "测试 book-log" + name + ",port:" + port;
  }
}
