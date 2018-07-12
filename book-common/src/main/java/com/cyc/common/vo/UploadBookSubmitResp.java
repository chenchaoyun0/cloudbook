package com.cyc.common.vo;

import java.util.List;

import com.cyc.common.po.Book;

import lombok.Data;

@Data
public class UploadBookSubmitResp extends BaseResp {
  /**
  *
  */
  private static final long serialVersionUID = 1L;
  
  private Book book;
  
  private  List<String> imgList;
}
