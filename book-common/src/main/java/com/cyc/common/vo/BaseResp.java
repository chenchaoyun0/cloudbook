 package com.cyc.common.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class BaseResp<T> implements Serializable{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  private int code=0;
  
  private String msg;
  
  private T data;
  
  public boolean success(){
    return this.code==0?true:false;
  }
  
}
