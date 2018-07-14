package com.cyc.bookweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cyc.bookweb.intercepter.UserIPIntercepter;
import com.cyc.bookweb.intercepter.UserLoginIntercepter;

/**
 * 
 * @author chenchaoyun
 * @date 2018/07/14
 */
@Configuration
public class CustemConfigurerAdapter extends WebMvcConfigurerAdapter {

  // 关键，将拦截器作为bean写入配置中
  @Bean
  public UserIPIntercepter userIPIntercepter() {
    return new UserIPIntercepter();
  }

  @Bean
  public UserLoginIntercepter userLoginIntercepter() {
    return new UserLoginIntercepter();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 将请求拦截器与Path请求路径对应起来并进行注册
    registry.addInterceptor(userIPIntercepter()).addPathPatterns("/**");
    registry.addInterceptor(userLoginIntercepter())//
      .addPathPatterns("/book/**")//
      .addPathPatterns("/ebook/**")//
      .addPathPatterns("/order/**")//
      .addPathPatterns("/**/admin/**");
  }
}
