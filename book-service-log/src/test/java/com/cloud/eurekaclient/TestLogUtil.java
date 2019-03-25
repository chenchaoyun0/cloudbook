package com.cloud.eurekaclient;

import com.megvii.dzh.utils.LogUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

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
    log.info("userVo:{}",LogUtil.formatLog(userVo));
    //2-集合/嵌套属性对象
    List<UserDog> userDogList = userVo.getUserDogList();
    log.info("getUserDogList:{}",LogUtil.formatLog(userDogList));
  }
}