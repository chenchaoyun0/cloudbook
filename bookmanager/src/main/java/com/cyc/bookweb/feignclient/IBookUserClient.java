package com.cyc.bookweb.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyc.common.po.User;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.SelectUserPagesReq;

@FeignClient(value = "book-user")
public interface IBookUserClient {

  @RequestMapping(value = "/existUserEmail", method = RequestMethod.POST)
  boolean existUserEmail(@RequestParam("userEmail") String userEmail);

  @RequestMapping(value = "/existUserName", method = RequestMethod.POST)
  boolean existUserName(@RequestParam("loginName") String loginName);

  @RequestMapping(value = "/selectUserPages", method = RequestMethod.POST)
  PagedResult<User> selectUserPages(@RequestBody SelectUserPagesReq req);

  @RequestMapping(value = "/insertSelective", method = RequestMethod.POST)
  void insertSelective(@RequestBody User user);

  @RequestMapping(value = "/activeUserStatus/{userCode}/{i}", method = RequestMethod.GET)
  void activeUserStatus(@PathVariable("i") int i,@PathVariable("userCode") String userCode);

  @RequestMapping(value = "/userLoginSubmit", method = RequestMethod.POST)
  User userLoginSubmit(@RequestParam("loginName") String loginName,@RequestParam("userPwd") String userPwd);

  @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
  User selectByPrimaryKey(@RequestParam("userId") String userId);

  @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
  int updatePwd(@RequestBody User user);

  @RequestMapping(value = "/updatePermission/{userId}", method = RequestMethod.GET)
  int updatePermission(@PathVariable("userId") String userId);
}
