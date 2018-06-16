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
public class BookBookApplication {
  private static final Logger log = LoggerFactory.getLogger(BookBookApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BookBookApplication.class, args);
    log.info("book-book 启动成功!");
  }

}