package com.cloud.eurekaclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cyc.BookBookApplication;
import com.cyc.common.po.GridfsImg;
import com.cyc.service.IBaseMongoRepository;


@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes={BookBookApplication.class})
public class SpringMongoTest {
  private static final Logger logger = LoggerFactory.getLogger(SpringMongoTest.class);
  
  @Autowired
  private GridFsTemplate gridFsTemplate;
//  @Autowired
//  private GridFSBucket gridFSBucket;
  @Autowired
  private IBaseMongoRepository baseMongoRepository;
  

  @Test
  public void find() throws Exception {
    OutputStream out = new FileOutputStream("E:\\chenchaoyun\\Desktop\\test.jpg");
    
    String id="5b472bcde65ef90abc9390e4";
    InputStream inputStream = baseMongoRepository.getInputStreamById(id);
    IOUtils.copy(inputStream, out);
  }
  @Test
  public void saveService() throws Exception {
    InputStream in= new FileInputStream(new File("E:\\job\\photo\\face.jpg"));
    String fileName="face.jpg";
    GridfsImg gridfsImg=new GridfsImg();
    gridfsImg.setIn(in);
    gridfsImg.setFileName(fileName);
    //
    String url = baseMongoRepository.saveImg(gridfsImg);
    logger.info("===================url:{}",url);
  }
}
