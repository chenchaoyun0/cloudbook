package com.cyc.common.base;

import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeForIpResp;

import lombok.Data;

@Data
public class ErrorCode {

  private String errorCode;
  private String errorMsg;

  public ErrorCode(String errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }
}
