package com.cyc.bookweb.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyc.common.vo.IndexHomeVo;

@FeignClient(value = "book-log")
public interface IBookLogService {
  @RequestMapping(value = "/indexHome", method = RequestMethod.GET)
  public IndexHomeVo sayHiFromClientOne(@RequestParam(value = "pageNo") Integer pageNo,
    @RequestParam(value = "pageSize") Integer pageSize);
}
