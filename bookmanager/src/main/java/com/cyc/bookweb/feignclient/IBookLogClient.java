package com.cyc.bookweb.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyc.common.po.TLog;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;

@FeignClient(value = "book-log")
public interface IBookLogClient {
  @RequestMapping(value = "/indexHome", method = RequestMethod.GET)
  public IndexHomeResp indexHome(@RequestParam(value = "pageNo") Integer pageNo,
    @RequestParam(value = "pageSize") Integer pageSize);
  /**
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @RequestMapping(value = "/indexHomeForIp", method = RequestMethod.GET)
  public IndexHomeForIpResp indexHomeForIp(@RequestParam(value = "userIp")String userIp, @RequestParam(value = "pageNo")Integer pageNo, @RequestParam(value = "pageSize")Integer pageSize);
  
  @RequestMapping(value = "/saveLog", method = RequestMethod.POST)
  public int saveLog(@RequestBody TLog tLog);
}
