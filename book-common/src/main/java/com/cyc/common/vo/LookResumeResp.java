package com.cyc.common.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class LookResumeResp extends BaseResp implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private long resumeCount;// 简历访问量

  private long totalcount;// 网站总访问量

  private long todayCount;// 当日访问量

  private long todayVisitorCount;// 当日访问人数

  private long totalVisitorCount;// 总访问人数

}
