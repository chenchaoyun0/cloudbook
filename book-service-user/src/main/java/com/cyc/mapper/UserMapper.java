package com.cyc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cyc.common.po.Book;
import com.cyc.common.po.User;

import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User>{
    /**
     * @Title: findUserBookListByUserId
     * @Description: 通过用户Id查询用户个人所有图书
     * @param userId
     * @return
     * @return: List<Book>
     */
    List<Book> findUserBookListByUserId(String userId);

    /**
     * 
     * @Title: existUserEmail
     * @Description: 验证邮箱是否重复
     * @param userEmail
     * @return
     * @return: Integer
     */
    Integer existUserEmail(String userEmail);

    /**
     * @Title: existUserName
     * @Description: 验证用户名是否重复
     * @param loginName
     *            登陆名
     * @return
     * @return: Integer
     */
    Integer existUserName(String loginName);

    /**
     * @Title: activeUserStatus
     * @Description: 用户激活
     * @param userStatus
     *            用户状态
     * @param userCode
     *            用户激活码
     * @return
     * @return: Integer
     */
    Integer activeUserStatus(@Param("userStatus") Integer userStatus, @Param("userCode") String userCode);

    /**
     * 
     * @Title: findByCode
     * @Description: 通过激活码查找用户
     * @param userCode
     * @return
     * @return: User
     */
    User findByCode(@Param("userCode") String userCode);

    /**
     * 
     * @Title: userLoginSubmit
     * @Description: 用户登录
     * @param loginName
     *            可以是用户名，邮箱，手机号
     * @return
     * @return: User
     */
    User userLoginSubmit(@Param("loginMes") String loginName);

    /**
     * 
     * @Title: selectUserPages
     * @Description: 管理员操作用户列表
     * @param user
     * @return
     * @return: List<User>
     */
    List<User> selectUserPages(@Param("user") User user);

    /**
     * 分配管理员权限
     * 
     * @param userId
     * @return
     */
    int updatePermission(String userId);
}