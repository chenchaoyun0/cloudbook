 package com.cyc.common.vo;

import com.cyc.common.po.TLog;
import com.github.pagehelper.PageInfo;

import lombok.Data;

@SuppressWarnings("rawtypes")
@Data
public class VisitorsResp extends BaseResp{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  private PageInfo<TLog> pageInfo;

}
