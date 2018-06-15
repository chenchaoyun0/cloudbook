package com.cyc.common.po;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.cyc.common.utils.images.HideImg;

import lombok.Data;

@Data
public class Book implements Serializable {

  private static final long serialVersionUID = 1L;

  private String bookId;

  private String userId;

  private Integer bookCountry;

  private String bookHouse;

  private String bookNo;
  @HideImg
  private String bookImg;

  private BufferedImage bookImage;

  private String bookName;

  private String bookAuthor;

  private Double bookPrice;

  private String bookDesc;

  private Integer bookCount;

  private String bookByTime;

  private Integer bookRemain;

  private String bookUploadTime;

  private int bookType;

  private Integer bookStatus;

  private Integer bookFlag;

  private User user;
}