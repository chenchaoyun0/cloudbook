package com.cloud.eurekaclient;

import com.cyc.common.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 描述:
 *
 * @author chenchaoyun
 * @create 2018-11-27 14:42
 */
@Slf4j
public class TestLogUtil {
  @Test
  public void test1(){
    //1-单一属性对象
    UserVo userVo = new UserVo();
    UserDog userDog = new UserDog();
    userVo.setUserDog(userDog);
    log.info("userVo:{}",LogUtil.formatLog(userVo));
    //2-集合/嵌套属性对象
    UserListAndMapVo userListAndMapVo = new UserListAndMapVo();
    userListAndMapVo.setUserVo(userVo);
    userListAndMapVo.add(userVo);
    userListAndMapVo.add(userVo);
    userListAndMapVo.put(userVo);
    userListAndMapVo.put(userVo);
    List<UserDog> userDogList=new ArrayList<>();
    userDogList.add(userDog);
    userListAndMapVo.setUserDogList(userDogList);
    userListAndMapVo.add(userDog);
    log.info("userListAndMapVo:{}",LogUtil.formatLog(userListAndMapVo));
    log.info("userListAndMapVo jsonFormatter:{}",LogUtil.jsonFormatter(LogUtil.formatLog(userListAndMapVo)));
  }
}