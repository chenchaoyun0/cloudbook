package com.cyc.common.po;

import java.io.Serializable;

import lombok.Data;
@Data
public class EBook implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ebookId;
    private String userId;
    private String ebookName;
    private Integer ebookCountry;
    private String ebookNo;
    private String ebookHouse;
    private String ebookImg;
    private String ebookDesc;
    private String ebookAuthor;
    private String ebookType;
    private Integer ebookSize;
    private String ebookPath;
    private String ebookUploadTime;
    private Integer ebookDownloadCount;
    private Integer ebookFlag;
    private User user;
}