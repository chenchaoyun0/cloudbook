package com.cyc.bookweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:spring/springmvc.xml" })
public class ConfigClass {

    public ConfigClass() {
        super();
        System.out.println("加载spring配置文件开始...AUTO ConfigClass");
    }

}
