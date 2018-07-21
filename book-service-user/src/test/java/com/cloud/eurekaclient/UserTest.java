package com.cloud.eurekaclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cyc.BookUserApplication;
import com.cyc.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = {BookUserApplication.class})
@Slf4j
public class UserTest {
  @Autowired
  private IUserService userService;
  
  @Test
  public void test() {
    boolean b = userService.existUserEmail("chenchaoyun@sttxtech.com");
    log.info("---->b:{}",b);
  }
}
