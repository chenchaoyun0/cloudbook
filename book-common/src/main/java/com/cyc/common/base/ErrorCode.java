package com.cyc.common.base;

public class ErrorCode {
  
  private String errorCode;
  private String errorMsg;

  public ErrorCode(String errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String toString() {
    return "错误信息:" + errorCode + ":" + errorMsg;
  }
}
