package com.cyc.common.po;

import java.io.Serializable;

import lombok.Data;
@Data
public class TLogKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String logId;

    private String userIp;

    public TLogKey() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TLogKey(String logId, String userIp) {
        super();
        this.logId = logId;
        this.userIp = userIp;
    }
}