package com.cyc.bookweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrixDashboard
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@ServletComponentScan
public class BookWebApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {

  private static final Logger log = LoggerFactory.getLogger(BookWebApplication.class);
  private static String contextPath = null;
  private static String port = null;

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(BookWebApplication.class);
  }

  public static void main(String[] args) throws Exception {
    new SpringApplicationBuilder(BookWebApplication.class).web(true).run(args);
    log.info(">>>>>图书管理系统主服务启动成功!");
  }

  public void customize(ConfigurableEmbeddedServletContainer container) {
    if (contextPath != null) {
      container.setContextPath(contextPath);
    }

    if (port != null) {
      container.setPort(Integer.valueOf(port));
    }
  }
}
