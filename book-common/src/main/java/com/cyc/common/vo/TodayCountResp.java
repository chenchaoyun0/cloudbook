 package com.cyc.common.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class TodayCountResp implements Serializable{

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  private long todayCount;
  
  private long todayVisitorCount;

}
