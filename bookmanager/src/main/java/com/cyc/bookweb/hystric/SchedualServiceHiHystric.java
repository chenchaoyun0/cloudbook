package com.cyc.bookweb.hystric;

import org.springframework.stereotype.Component;

import com.cyc.bookweb.feignclient.IBookLogClient;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.TLog;
import com.cyc.common.vo.IndexHomeForIpResp;
import com.cyc.common.vo.IndexHomeResp;
import com.cyc.common.vo.SelectBlackListResp;
import com.cyc.common.vo.VisitorsResp;
import com.github.pagehelper.PageInfo;

@Component
public class SchedualServiceHiHystric implements IBookLogClient {
  @Override
  public IndexHomeResp indexHome(Integer pageNo, Integer pageSize) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IndexHomeForIpResp indexHomeForIp(String userIp, Integer pageNo, Integer pageSize) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int saveLog(TLog tLog) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long totalPathCount(String path) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int saveBlackLisEntity(BlackLisEntity blackLisEntity) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int updateBlackLisEntitySelective(BlackLisEntity blackLisEntity) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public BlackLisEntity selectBlackLisEntityByIp(String ip) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VisitorsResp visitors() {
    VisitorsResp resp = new VisitorsResp();
    resp.setCode(-1);
    resp.setMsg("出异常啦  走 Hystrix ...");
    resp.setPageInfo(new PageInfo<>());
    return resp;
  }

  @Override
  public SelectBlackListResp selectBlackList() {
    // TODO Auto-generated method stub
    return null;
  }

}
