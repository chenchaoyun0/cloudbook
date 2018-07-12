package com.cyc.common.base;

import lombok.Data;

@Data
public class ErrorCode {

  public static final int ERROR_CODE=-1;
  
  private String errorCode;
  private String errorMsg;

  public ErrorCode(String errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }
}
