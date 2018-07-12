package com.cloud.eurekaclient;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cyc.BookLogApplication;
import com.cyc.common.po.BlackLisEntity;
import com.cyc.common.utils.time.DateConvertUtils;
import com.cyc.mapper.BlackListMapper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes={BookLogApplication.class})
@Slf4j
public class BlackListTest {
  @Autowired
  private BlackListMapper blackListMapper;

  @Test
  public void visitors() {
    String lasttime = DateConvertUtils.format(new Date(), DateConvertUtils.DATE_TIME_FORMAT);
    String path ="/test";
    //
    Example example = new Example(BlackLisEntity.class);
    example.createCriteria().andEqualTo("ip", "203.208.60.168");
    BlackLisEntity blackLisEntity = blackListMapper.selectOneByExample(example);

    if (blackLisEntity != null) {
      // 更新次数
      blackLisEntity.setBrowserAndVersion("setBrowserAndVersion");
      blackLisEntity.setCount(blackLisEntity.getCount() + 1);
      blackLisEntity.setLasttime(lasttime);
      blackLisEntity.setPath(path);
      //
      Example exampleUpdate = new Example(BlackLisEntity.class);
      exampleUpdate.createCriteria().andEqualTo("id", 1l);
      int updateByPrimaryKey = blackListMapper.updateByExampleSelective(blackLisEntity, exampleUpdate);
      log.info("已被禁止访问,updateByPrimaryKey:{}",  updateByPrimaryKey);

    }
  }

}
