package com.cloud.eurekaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

/**
 * 描述:
 *
 * @author chenchaoyun
 * @create 2018-11-27 14:51
 */
@Data
public class UserListAndMapVo {

  private UserVo userVo;

  private String userName="chenchaoyun";

  private String userPassword="BORw0KGgoA";

  private String userPwd="qwertyuiop";

  private String userImg="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAioAAAEqCAYAAAA72HsuAAAgAElEQVR4AeydBdQtSXW2m+Ti7gzu7u4e3IPN4ME9aLD8uHtwd9fBBofgMFjwwW3QEBwGSMK/nuY+N/v";


  private List<UserVo> list = new ArrayList<>();



  private Map<String, UserVo> map = new HashMap<>();


  public boolean add(UserVo userVo) {
    return this.list.add(userVo);
  }


  public UserVo put(UserVo userVo) {
    return this.map.put(UUID.randomUUID().toString().replaceAll("-", ""), userVo);
  }

}