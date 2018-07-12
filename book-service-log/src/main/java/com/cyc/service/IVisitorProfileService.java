package com.cyc.service;

import java.util.List;

import com.cyc.common.po.VisitorProfile;
import com.cyc.common.service.IBaseService;

public interface IVisitorProfileService extends IBaseService<VisitorProfile> {
  
  public List<VisitorProfile> visitors();
}
