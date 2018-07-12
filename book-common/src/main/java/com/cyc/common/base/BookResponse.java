package com.cyc.common.base;

import java.io.Serializable;

import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeForIpResp;

import lombok.Data;

/**
 * 此类描述的是：业务组件系统对外提供的服务响应标头信息
 */
@Data
public abstract class BookResponse implements Serializable {
  /** 
   * 
   */
  private static final long serialVersionUID = 1L;
  private String errorCode;
  private String errorMsg;

  public final static String SUCESS_CODE = "200";
  public final static String ERROR_CODE = "500";

  public BookResponse() {
    errorCode = SUCESS_CODE;
  }

  public BookResponse(String errorCode) {
    this.errorCode = errorCode;
  }

  public void setErrorCode() {
    this.errorCode = BookResponse.ERROR_CODE;
  }

  public boolean succeeded() {
    return SUCESS_CODE.equals(errorCode);
  }
}
