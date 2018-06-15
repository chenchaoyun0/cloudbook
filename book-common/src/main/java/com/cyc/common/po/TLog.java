package com.cyc.common.po;

import java.io.Serializable;

import lombok.Data;
@Data
public class TLog extends TLogKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;

    private String userNickName;

    private String userAddress;

    private String userJwd;

    private String module;

    private String action;

    private Long actionTime;

    private String operTime;

    private Long count;

    public TLog() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TLog(String userName, String userNickName, String userAddress, String userJwd, String module, String action,
            Long actionTime, String operTime, Long count) {
        super();
        this.userName = userName;
        this.userNickName = userNickName;
        this.userAddress = userAddress;
        this.userJwd = userJwd;
        this.module = module;
        this.action = action;
        this.actionTime = actionTime;
        this.operTime = operTime;
        this.count = count;
    }

}