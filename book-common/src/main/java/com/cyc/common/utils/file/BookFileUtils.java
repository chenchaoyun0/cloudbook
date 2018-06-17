package com.cyc.common.utils.file;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyc.common.po.TImg;
import com.cyc.common.utils.exception.UserException;

/**
 * 
 * @Description 操作nfs文件
 * @author chenchaoyun[chenchaoyun@sttxtech.com]
 * @date 2017年6月23日 上午10:12:37
 */
public class BookFileUtils {
  private static final Logger log = LoggerFactory.getLogger(BookFileUtils.class);
  public static String jspImgSrc = "data:image/jpg;base64,";
  private static String nfsUrl = null;
  private static String[] imgTypes = null;
  static {
    imgTypes = new String[] {"jpg", "png", "jpeg", "gif", "bmp", "jpe", "tif", "tiff"};
  }

  public static String[] getImgTypes() {
    return imgTypes;
  }

  public static String getJspImgSrc() {
    return jspImgSrc;
  }

  public static String getNfsUrl() {
    return nfsUrl;
  }

  /**
   * 上传文件到NFS
   * 
   * @Description
   * @param in 输入流
   * @param out 输出流
   * @return 文件大小
   * @throws UserException
   */

  /**
   * 复制nfs文件到本地
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 如:nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @param localFileName 本地文件绝对路径 如:/u01/app/ccbcusr/test/ad.jpg
   * @return 文件大小
   * @throws UserException
   */

  /**
   * 读取nfs文件 进流
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 如:nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @return
   * @throws UserException
   */

  /**
   * 读取文件字节
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 如:nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @return 字节
   * @throws UserException
   */

  /**
   * 读取文件字节
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 如:nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @return 字节
   * @throws UserException
   */
  public static byte[] readNfsStream2Byte(InputStream in) throws UserException {
    byte[] byteArray = null;
    try {
      byteArray = IOUtils.toByteArray(in);
    } catch (Exception e) {
      log.error("读取nfs文件为字节异常", e);
      throw new UserException("USPS0104", "读取nfs文件为字节异常");
    }
    return byteArray;
  }

  /**
   * 删除nfs文件
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 如:nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @return true-成功删除，false-失败
   * @throws UserException
   */

  /**
   * nfs mkdir
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @return true-存在，false-不存在
   */

  /**
   * nfs 文件是否存在
   * 
   * @Description
   * @param nfsFileName 远程文件绝对路径 nfs://192.168.1.xxx:/u01/app/image/ad/ad.jpg
   * @return true-存在，false-不存在
   */

  /**
   * 将图片读取为base64 字符串
   * 
   * @Description
   * @param nfsFileName
   * @return
   * @throws UserException
   */

  /**
   * 将图片读取为base64 字符串
   * 
   * @Description
   * @param nfsFileName
   * @return
   * @throws UserException
   */
  public static String getImageBase64Str(InputStream in) throws UserException {
    byte[] b = readNfsStream2Byte(in);
    return jspImgSrc + getBase64Str(b);
  }

  /**
   * 将图片读取为base64 字符串
   * 
   * @Description
   * @param nfsFileName
   * @return
   * @throws UserException
   */
  public static String getImageBase64Str(byte[] b) throws UserException {
    String base64Str = getBase64Str(b);
    return jspImgSrc + base64Str;
  }

  /**
   * 将图片读取为base64 字符串
   * 
   * @Description
   * @param nfsFileName
   * @return
   * @throws UserException
   */
  public static String getBase64Str(byte[] b) throws UserException {
    String base64String = Base64.encodeBase64String(b);
    // @SuppressWarnings("restriction")
    // String base64String = new BASE64Encoder().encode(b);
    return base64String;
  }

  public static byte[] base64Str2Byte(String base64String) throws UserException {
    return Base64.decodeBase64(base64String);
  }

  public static List<String> getImageBase64StrList(List<TImg> imgList) {
    List<String> base64StrList = new ArrayList<>();
    for (TImg tImg : imgList) {
      String imgPath = tImg.getImgPath();
      base64StrList.add(imgPath);
    }
    return base64StrList;
  }

  public static boolean isImgFile(String fileName) {
    if (ArrayUtils.contains(imgTypes, fileName.toLowerCase())) {
      return true;
    }
    return false;
  }

}
