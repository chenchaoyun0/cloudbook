package com.cloud.eurekaclient;

import com.megvii.dzh.annotations.HideAnn;
import com.megvii.dzh.annotations.HideCollection;
import com.megvii.dzh.annotations.HideImg;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 描述:
 *
 * @author chenchaoyun
 * @create 2018-11-27 14:42
 */
@Data
public class UserVo {


  public UserVo() {
    this.userDogList.add(new UserDog());
    this.userDogList.add(new UserDog());
  }

  private String userName = "chenchaoyun";

  @HideImg
  private String userPassword = "BORw0KGgoA";

  @HideAnn
  private String userPwd = "qwertyuiop";

  @HideImg
  private String userImg = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAioAAAEqCAYAAAA72HsuAAAgAElEQVR4AeydBdQtSXW2m+Ti7gzu7u4e3IPN4ME9aLD8uHtwd9fBBofgMFjwwW3QEBwGSMK/nuY+N/v";

  @HideCollection
  private List<UserDog> userDogList = new ArrayList<>();


}
@Data
class UserDog {

  @HideImg
  private String dogImg = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAioAAAEqCAYAAAA72HsuAAAgAElEQVR4AeydBdQtSX";

}