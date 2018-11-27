package com.cloud.eurekaclient;

import com.cyc.common.utils.images.HideCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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


  @HideCollection
  private List<UserVo> list = new ArrayList<>();


  @HideCollection
  private List<UserDog> userDogList=null;

  @HideCollection
  private Map<String, UserVo> map = new HashMap<>();

  @HideCollection
  private Set<UserDog> userDogSet=new HashSet<>();

  public boolean add(UserVo userVo) {
    return this.list.add(userVo);
  }

  public boolean add(UserDog userDog) {
    return this.userDogSet.add(userDog);
  }

  public UserVo put(UserVo userVo) {
    return this.map.put(UUID.randomUUID().toString().replaceAll("-", ""), userVo);
  }

}