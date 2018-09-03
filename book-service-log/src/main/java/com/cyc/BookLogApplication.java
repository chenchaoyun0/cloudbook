package com.cyc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.cyc.common.utils.spring.SpringUtils;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@MapperScan("com.cyc.mapper")
public class BookLogApplication extends SpringBootServletInitializer
  implements EmbeddedServletContainerCustomizer, ApplicationListener<ContextRefreshedEvent> {
  private static final Logger log = LoggerFactory.getLogger(BookLogApplication.class);
  private static String contextPath = null;
  private static String port = null;

  public static void main(String[] args) {
    SpringApplication.run(BookLogApplication.class, args);
    log.info("book-log 启动成功!");
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("开机服务执行的操作....");
    DataSource dataSource = SpringUtils.getBean("dataSource", DataSource.class);
    log.info("dataSource:{}", dataSource);
    try {
      DatabaseMetaData databaseMetaData = dataSource.getConnection().getMetaData();
      String url = databaseMetaData.getURL();
      log.info("url:{}", url);
      String userName = databaseMetaData.getUserName();
      log.info("userName:{}", userName);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void customize(ConfigurableEmbeddedServletContainer container) {
    if (contextPath != null) {
      container.setContextPath(contextPath);
    }

    if (port != null) {
      container.setPort(Integer.valueOf(port));
    }
  }
}