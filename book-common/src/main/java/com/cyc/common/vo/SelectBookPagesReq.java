package com.cyc.common.vo;

import com.cyc.common.po.Book;

import lombok.Data;
@Data
public class SelectBookPagesReq extends BaseReq {

  
  public SelectBookPagesReq() {
    super();
     // TODO Auto-generated constructor stub
  }
  public SelectBookPagesReq(Book book, Integer pageNo, Integer pageSize) {
    super();
    this.book = book;
    this.pageNo = pageNo;
    this.pageSize = pageSize;
  }
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
