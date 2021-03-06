package com.cyc.bookweb.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyc.bookweb.hystric.SchedualServiceHiHystric;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.TLog;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;
import com.cyc.common.vo.SelectBlackListResp;
import com.cyc.common.vo.VisitorsResp;

@FeignClient(value = "book-log",fallback = SchedualServiceHiHystric.class)
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
  
  @RequestMapping(value = "/totalPathCount", method = RequestMethod.GET)
  public long totalPathCount(@RequestParam(value = "path")String path);
  
  @RequestMapping(value = "/saveBlackLisEntity", method = RequestMethod.POST)
  public int saveBlackLisEntity(@RequestBody BlackLisEntity blackLisEntity);
  
  
  @RequestMapping(value = "/updateBlackLisEntitySelective", method = RequestMethod.POST)
  public int updateBlackLisEntitySelective(@RequestBody BlackLisEntity blackLisEntity);
  
  
  @RequestMapping(value = "/selectBlackLisEntityByIp", method = RequestMethod.GET)
  public BlackLisEntity selectBlackLisEntityByIp(@RequestParam(value = "ip") String ip);
  
  @RequestMapping(value = "/visitors", method = RequestMethod.GET)
  public VisitorsResp visitors();
  
  @RequestMapping(value = "/selectBlackList", method = RequestMethod.GET)
  public SelectBlackListResp selectBlackList();
  
  
}
