 package com.cyc.common.vo;

import com.cyc.common.po.BlackLisEntity;
import com.github.pagehelper.PageInfo;

import lombok.Data;

@SuppressWarnings("rawtypes")
@Data
public class SelectBlackListResp extends BaseResp{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  private PageInfo<BlackLisEntity> pageInfo;

}
