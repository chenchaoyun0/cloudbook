package com.cloud.eurekaclient;

import com.cyc.common.utils.images.HideAnn;
import com.cyc.common.utils.images.HideImg;
import lombok.Data;

/**
 * 描述:
 *
 * @author chenchaoyun
 * @create 2018-11-27 14:42
 */
@Data
public class UserVo {


  private String userName="chenchaoyun";

  @HideImg
  private String userPassword="BORw0KGgoA";

  @HideAnn
  private String userPwd="qwertyuiop";

  @HideImg
  private String userImg="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAioAAAEqCAYAAAA72HsuAAAgAElEQVR4AeydBdQtSXW2m+Ti7gzu7u4e3IPN4ME9aLD8uHtwd9fBBofgMFjwwW3QEBwGSMK/nuY+N/v";

  private UserDog userDog;

}