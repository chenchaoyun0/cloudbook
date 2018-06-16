package com.cyc.common.vo;

import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;

import lombok.Data;

@Data
public class IndexHomeForIpVo extends BaseVo {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  private PagedResult<TLog> pages;
  
  private Long totalcount;

}
