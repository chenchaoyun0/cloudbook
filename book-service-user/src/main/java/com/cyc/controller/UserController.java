package com.cyc.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.po.User;
import com.cyc.common.utils.SHA;
import com.cyc.common.utils.TelUtil;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.SelectUserPagesReq;
import com.cyc.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {
  @Autowired
  private IUserService userService;

  @Value("${server.port}")
  private String port;

  @RequestMapping("/info")
  public String home() {
    PagedResult<User> pagedResult = userService.selectUserPages(new User(), 1, 100);
    String jsonString = JSONObject.toJSONString(pagedResult);
    return "测试 book-user" + ",port:" + port + ";测试响应:" + jsonString;
  }

  @RequestMapping(value = "/existUserEmail", method = RequestMethod.POST)
  public @ResponseBody boolean existUserEmail(String userEmail) {
    return userService.existUserEmail(userEmail);
  }

  @RequestMapping(value = "/existUserName", method = RequestMethod.POST)
  public @ResponseBody boolean existLoginName(String loginName) {
    return userService.existUserName(loginName);
  }

  @RequestMapping(value = "/userTelHome", method = RequestMethod.POST)
  public @ResponseBody String userTelHome(HttpServletRequest request, String userTel)
    throws UnsupportedEncodingException {
    String telHome = TelUtil.getTelHome(userTel);

    return telHome;
  }

  @RequestMapping(value = "/userRegistSubmit", method = RequestMethod.POST)
  public void userRegistSubmit(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
    userService.insertSelective(user);
  }

  @RequestMapping(value = "/activeUserStatus/{userCode}/{i}", method = RequestMethod.GET)
  public void activeUserStatus(@PathVariable("i") int i, @PathVariable("userCode") String userCode) throws IOException {
    userService.activeUserStatus(i, userCode);
  }

  @RequestMapping(value = "/userLoginSubmit", method = RequestMethod.POST)
  public User userLoginSubmit(String loginName, String userPwd) {
    User user = null;
    try {
      return userService.userLoginSubmit(loginName, userPwd);
    } catch (UserException e) {
    }
    return user;
  }

  @RequestMapping("/selectByPrimaryKey")
  @ResponseBody
  public User selectByPrimaryKey(String userId) {
    return userService.selectByPrimaryKey(userId);
  }

  @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
  @ResponseBody
  public int updatePwd(HttpServletRequest request, String userId, String loginName, String oripassword,
    String nowpassword, String verifyCode) {
    // 验证密码是否正确
    User user = null;
    try {
      user = userService.userLoginSubmit(loginName, oripassword);
    } catch (UserException e) {
      return -2;
    }
    // 对密码经行加密后加入
    user.setUserId(userId);
    user.setUserPwd(SHA.getSHA256(nowpassword));
    int i = userService.updatePwd(user);
    return i;
  }

  @RequestMapping("/selectUserPages")
  @ResponseBody
  public PagedResult<User> selectUserPages(@RequestBody SelectUserPagesReq req) {
    Integer pageNo = req.getPageNo();
    Integer pageSize = req.getPageSize();
    User user = req.getUser();
    PagedResult<User> list = userService.selectUserPages(user, pageNo, pageSize);
    return list;
  }

  @RequestMapping("/updatePermission/{userId}")
  @ResponseBody
  public int updatePermission(@PathVariable("userId") String userId) {
    return userService.updatePermission(userId);
  }
}
