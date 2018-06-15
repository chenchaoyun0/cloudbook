package com.cyc.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserException extends RuntimeException {
  private static final Logger log = LoggerFactory.getLogger(UserException.class);
  /**
   * @fieldName: serialVersionUID
   * @fieldType: long
   * @Description: TODO
   */
  private static final long serialVersionUID = 1L;

  public UserException() {
    super();
  }

  public UserException(String message) {
    super(message);
  }

  public UserException(String message, Throwable e) {
    super(message, e);
  }

  public UserException(Throwable e) {
    super(e);
  }

  public UserException(String errorCode, String message) {
    super(message);
    log.error(">>>>>>>>>>>>>>>>>>>>>errorCode:{}", errorCode);
  }

}
