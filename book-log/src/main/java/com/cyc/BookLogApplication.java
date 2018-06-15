package com.cyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BookLogApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookLogApplication.class, args);
  }

}