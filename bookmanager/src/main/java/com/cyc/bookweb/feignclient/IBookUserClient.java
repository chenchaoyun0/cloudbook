package com.cyc.bookweb.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.cyc.common.po.User;
import com.cyc.common.utils.pages.PagedResult;

//@FeignClient(value = "book-user")
public interface IBookUserClient {

  boolean existUserEmail(String userEmail);

  boolean existUserName(String loginName);

  PagedResult<User> selectUserPages(Object object, Object object2, Object object3);

  void insertSelective(User user);

  void activeUserStatus(int i, String userCode);

  User userLoginSubmit(String loginName, String userPwd);

  User selectByPrimaryKey(String userId);

  int updatePwd(User user);

  int updatePermission(String userId);

  String chatWithRobot(String user_say);
  
}
