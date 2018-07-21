package com.cyc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.po.Book;
import com.cyc.common.po.User;
import com.cyc.common.utils.SHA;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.pages.BeanUtil;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.mapper.UserMapper;
import com.cyc.service.IUserService;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("userService")
public class UserServiceImpl implements IUserService {
  @Autowired
  private UserMapper userMapper;
  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  public User selectByPrimaryKey(String userId) {
    Example example = new Example(User.class);
    example.createCriteria().andEqualTo("userId", userId);
    User user = userMapper.selectOneByExample(example);
    return user;
  }


  public List<Book> findUserBookListByUserId(String userId) {
    return userMapper.findUserBookListByUserId(userId);
  }

  public boolean existUserEmail(String userEmail) {
    if (userMapper.existUserEmail(userEmail) > 0) {

      return true;
    }
    return false;
  }

  public int insertSelective(User record) {
    try {
      return userMapper.insertSelective(record);
    } catch (Exception e) {
      throw new UserException("注册失败");
    }
  }

  public boolean existUserName(String loginName) {
    if (userMapper.existUserName(loginName) > 0) {
      return true;
    }
    return false;
  }

  public Integer activeUserStatus(Integer userStatus, String userCode) {

    User user = userMapper.findByCode(userCode);
    if (user == null) {
      throw new UserException("您的操作不当，激活码无效！3秒后跳转到主页。如若您的操作不当行为超过3次，您将无法访问本网站!谢谢合作");
    }
    if (user.getUserStatus() == 1) {
      throw new UserException("您已经成功激活，不可再次点击。3秒后跳转到登录页面。");
    }
    return userMapper.activeUserStatus(userStatus, userCode);
  }

  public User userLoginSubmit(String loginName, String userPwd) {

    User user = userMapper.userLoginSubmit(loginName);
    if (user == null) {
      throw new UserException("用户不存在");
    }
    if (!user.getUserPwd().equals(SHA.getSHA256(userPwd))) {
      throw new UserException("密码有误");
    }
    if (user.getUserStatus() == 0) {
      throw new UserException(user.getLoginName() + ",您尚未激活，请前往邮箱" + user.getUserEmail() + "激活");
    }

    String userHead = user.getUserHead();
    // String imageBase64Str = NfsFileUtils.getImageBase64Str(NfsFileUtils.getNfsUrl() + userHead);
    user.setUserHead(userHead);
    return user;
  }

  public PagedResult<User> selectUserPages(User user, Integer pageNo, Integer pageSize) {
    pageNo = pageNo == null ? (Integer)1 : pageNo;
    pageSize = pageSize == null ? (Integer)8 : pageSize;
    PageHelper.startPage(pageNo, pageSize);// 告诉插件开始分页

    List<User> list = userMapper.selectUserPages(user);

    log.info("list:{}", JSONObject.toJSON(list.size()));

    PagedResult<User> bookPagedResult = BeanUtil.toPagedResult(list);

    return bookPagedResult;
  }

  public int updatePermission(String userId) {
    return userMapper.updatePermission(userId);
  }

  public int updatePwd(User user) {
    return userMapper.updateByPrimaryKeySelective(user);
  }
}
