 package com.cyc.common.vo;

import com.cyc.common.po.Book;
import com.cyc.common.po.User;

import lombok.Data;

@Data
public class UploadBookSubmitReq extends BaseReq{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  
  private User user;
  
  private Book book;
  
}
