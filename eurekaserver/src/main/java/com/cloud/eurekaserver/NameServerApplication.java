package com.cloud.eurekaserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;

@EnableEurekaServer
@SpringBootApplication
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class NameServerApplication {
  private static final Logger log = LoggerFactory.getLogger(NameServerApplication.class);

  public static void main(String[] args) {
    new SpringApplicationBuilder(new Object[] {NameServerApplication.class}).web(true).run(args);
    log.info("eureka server 启动成功!");
  }
}