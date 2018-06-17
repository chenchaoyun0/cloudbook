package com.cyc.common.vo;

import com.cyc.common.po.Book;

import lombok.Data;
@Data
public class SelectBookPagesReq extends BaseReq {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private Book book;
  private String loginName;
  private Integer pageNo;
  private Integer pageSize;
  private String userId;
}
