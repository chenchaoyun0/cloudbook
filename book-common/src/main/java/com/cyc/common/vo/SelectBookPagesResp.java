 package com.cyc.common.vo;

import com.cyc.common.po.Book;
import com.cyc.common.utils.pages.PagedResult;

import lombok.Data;
@Data
public class SelectBookPagesResp extends BaseResp{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private  PagedResult<Book> pages;
}
