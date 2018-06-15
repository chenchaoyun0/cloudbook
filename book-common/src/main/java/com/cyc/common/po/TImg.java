package com.cyc.common.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
@Data
public class TImg implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imgId;

    private String linkId;

    private String linkType;

    private String imgPath;

    private String createUser;

    private Date createTime;

    private String lastModifyUser;

    private Date lastModifyTime;
}