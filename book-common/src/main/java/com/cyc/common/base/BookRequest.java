package com.cyc.common.base;

import java.io.Serializable;

import com.cyc.common.po.TLog;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.IndexHomeForIpResp;

import lombok.Data;

/**
 * 此类描述的是： 业务组件系统对外提供的服务请求标头信息
 */
@Data
public abstract class BookRequest implements Serializable {
    /** 
     * 
     */
    private static final long serialVersionUID = 1L;

}
