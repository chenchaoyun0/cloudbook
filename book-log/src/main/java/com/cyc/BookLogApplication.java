package com.cyc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.cyc.mapper")
public class BookLogApplication {
  private static final Logger log = LoggerFactory.getLogger(BookLogApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BookLogApplication.class, args);
    log.info("book-log 启动成功!");
  }

}