package com.cyc.common.base;

import java.io.Serializable;

/**
 * 此类描述的是： 业务组件系统对外提供的服务请求标头信息
 */
public abstract class BookRequest implements Serializable {
    /** 
     * 
     */
    private static final long serialVersionUID = 1L;

    private String serviceId;// 交易码
    private String flowNo;// 流水号
    private String tradeTime;// 交易时间
    private String sysFlag;// 渠道标识
    private String assetSide;// 资产标识 
    private String capitalSide;// 资金标识
    
    private String serviceMethod;
    
    

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getSysFlag() {
        return sysFlag;
    }

    public void setSysFlag(String sysFlag) {
        this.sysFlag = sysFlag;
    }

    public String getAssetSide() {
        return assetSide;
    }

    public void setAssetSide(String assetSide) {
        this.assetSide = assetSide;
    }

  public String getCapitalSide() {
    return capitalSide;
  }

  public void setCapitalSide(String capitalSide) {
    this.capitalSide = capitalSide;
  }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

}
