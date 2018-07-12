package com.cyc.common.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class LookResumeReq extends BaseReq implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String appName;
  private String appCodeName;
  private String platform;
  private String appVersion;
  private String userAgent;
  private String product;
  private String cookieEnabled;
  private String cpuClass;
  private String oscpu;
  private String productSub;
  private String userProfile;

}