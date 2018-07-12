 package com.cyc.mapper;

 import java.util.List;

import com.cyc.common.po.VisitorProfile;

import tk.mybatis.mapper.common.Mapper;

 public interface VisitorProfileMapper extends Mapper<VisitorProfile> {
   
   public List<VisitorProfile> visitors();
 }