package com.cyc.bookweb.hystric;

import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric{

  public String sayHiFromClientOne(String name) {
    return "sorry 异常啦啦啦" + name;
  }

}
