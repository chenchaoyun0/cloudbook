package com.cyc.common.vo;

import com.cyc.common.po.User;

import lombok.Data;

@Data
public class SelectUserPagesReq extends BaseReq {
  /**
  *
  */
  private static final long serialVersionUID = 1L;
  
  

  public SelectUserPagesReq(User user, Integer pageNo, Integer pageSize) {
    super();
    this.user = user;
    this.pageNo = pageNo;
    this.pageSize = pageSize;
  }
  public SelectUserPagesReq() {
    super();
  }
  private User user;
  private Integer pageNo;
  private Integer pageSize;
}
