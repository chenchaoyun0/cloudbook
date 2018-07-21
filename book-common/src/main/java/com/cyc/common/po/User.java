package com.cyc.common.po;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Table;

import lombok.Data;
@Data
@Table(name = "t_user")
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    private String userId;

    private String loginName;

    private String realName;

    private Integer userSex;

    private String userPwd;

    private String userEmail;

    private String userTel;

    private String userBirthday;

    private String userHead;

    private String userCode;

    private Integer userStatus;

    private String userRegisttime;

    private int userRole;

    private Integer userFlag;

    /* 封装类 */
    private List<Book> bookList;
    private List<OrderItem> orderList;
    private List<EBook> eBookList;

    
    private boolean isAccountNonExpired = true;  
    
    private boolean isAccountNonLocked = true;  
  
    private boolean isCredentialsNonExpired = true;  
  
    private boolean isEnabled = true;  
  
}