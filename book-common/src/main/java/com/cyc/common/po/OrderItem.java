package com.cyc.common.po;

import java.io.Serializable;

import lombok.Data;
@Data
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemId;

    private String bookId;

    private String userId;

    private Integer lendCount;

    private String lendTime;

    private String returnTime;

    private Double itemTotal;

    private Integer itemStatus;

    private Integer itemFlag;

    private Book book;
    private User user;
}