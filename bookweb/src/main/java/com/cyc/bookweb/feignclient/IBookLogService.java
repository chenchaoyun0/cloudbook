package com.cyc.bookweb.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyc.common.vo.IndexHomeForIpVo;
import com.cyc.common.vo.IndexHomeVo;

@FeignClient(value = "book-log")
public interface IBookLogService {
  @RequestMapping(value = "/indexHome", method = RequestMethod.GET)
  public IndexHomeVo indexHome(@RequestParam(value = "pageNo") Integer pageNo,
    @RequestParam(value = "pageSize") Integer pageSize);
  /**
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @RequestMapping(value = "/indexHomeForIp", method = RequestMethod.GET)
  public IndexHomeForIpVo indexHomeForIp(@RequestParam(value = "userIp")String userIp, @RequestParam(value = "pageNo")Integer pageNo, @RequestParam(value = "pageSize")Integer pageSize);
}
