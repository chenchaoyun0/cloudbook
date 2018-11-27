package com.cyc.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cyc.common.utils.images.HideAnn;
import com.cyc.common.utils.images.HideCollection;
import com.cyc.common.utils.images.HideImg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * 描述: 日志打印
 *
 * @author chenchaoyun
 * @create 2018/11/27
 */
@Slf4j
public class LogUtil {

  private static final String HIDE_PWD = "*******";

  /**
   * 美化json字符串
   *
   * @param uglyJSONString json 字符串
   */
  public static String jsonFormatter(String uglyJSONString) {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      JsonParser jp = new JsonParser();
      JsonElement je = jp.parse(uglyJSONString);
      return gson.toJson(je);
    } catch (Exception e) {
      log.error("jsonFormatter error", e);
    }
    return uglyJSONString;
  }

  /**
   * 过滤obj中敏感属性
   */
  public static String formatLog(Object obj) {
    if (ObjectUtils.isEmpty(obj)) {
      return "null";
    }
    String jsonStr = JSONObject.toJSONString(obj);
    //最终返回的obj json
    JSONObject objCopy = JSONObject.parseObject(jsonStr);
    List<Field> fields = getFieldListByClass(obj.getClass());
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object fieldVal = field.get(obj);
        hideProperties(objCopy, field, fieldVal);//敏感信息隐藏
      } catch (IllegalArgumentException | IllegalAccessException e) {
        log.error("formatLog error", e);
        return jsonStr;
      }
    }
    return objCopy.toJSONString();
  }

  private static boolean hasHideImg(Field field) {
    return field.getAnnotation(HideImg.class) != null;
  }

  private static boolean hasHideAn(Field field) {
    return field.getAnnotation(HideAnn.class) != null;
  }

  private static boolean hasHideCollection(Field field) {
    return field.getAnnotation(HideCollection.class) != null;
  }

  /**
   * 敏感信息隐藏
   *
   * @Description
   */
  private static void hideProperties(JSONObject objCopy, Field field, Object fieldVal) throws IllegalAccessException {
    String fieldName = field.getName();
    if (isBasicType(fieldVal)) {
      //基本类型
      int length = fieldVal.toString().length();
      if ((hasHideImg(field))) {
        objCopy.put(field.getName(), StringUtils.substring((fieldVal.toString()), 0, 20) + HIDE_PWD + StringUtils.substring((fieldVal.toString()), length - 11, length - 1));
      } else if ((hasHideAn(field))) {
        objCopy.put(field.getName(), HIDE_PWD);
      } else {
        objCopy.put(field.getName(), fieldVal);
      }
    } else if (fieldVal instanceof Collection) {
      //Collection类型
      Collection objCollection = (Collection) fieldVal;
      if (hasHideCollection(field)) {
        int size = objCollection.size();
        JSONArray array = new JSONArray();
        if (size > 0) {
          for (Object item : objCollection) {
            String formatLog = formatLog(item);
            JSONObject jsonObj = JSONObject.parseObject(formatLog);
            array.add(jsonObj);
            break;
          }
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("msg", "collection total size[" + size + "]");
          array.add(jsonObject);
        }
        objCopy.put(fieldName, array);
      } else {
        JSONArray array = new JSONArray();
        for (Object item : objCollection) {
          String formatLog = formatLog(item);
          JSONObject jsonObj = JSONObject.parseObject(formatLog);
          array.add(jsonObj);
        }
        objCopy.put(fieldName, array);
      }
    } else if (fieldVal instanceof Map) {
      //Map类型
      Map map = (Map) fieldVal;
      if (hasHideCollection(field)) {
        int size = map.size();
        JSONObject jsonObject = new JSONObject();
        if (size > 0) {
          for (Entry<Object, Object> objectEntry : (Set<Entry<Object, Object>>) (map.entrySet())) {
            Object key = objectEntry.getKey();
            Object item = objectEntry.getValue();
            String formatLog = formatLog(item);
            JSONObject parseObject = JSONObject.parseObject(formatLog);
            jsonObject.put(key.toString(), parseObject);
            break;
          }
          jsonObject.put("msg", "map total size[" + size + "]");
        }
        objCopy.put(fieldName, jsonObject);
      } else {
        JSONObject jsonObj = new JSONObject();
        for (Entry<Object, Object> objectEntry : (Set<Entry<Object, Object>>) (map.entrySet())) {
          Object key = objectEntry.getKey();
          Object item = objectEntry.getValue();
          String formatLog = formatLog(item);
          JSONObject parseObject = JSONObject.parseObject(formatLog);
          jsonObj.put(key.toString(), parseObject);
        }
        objCopy.put(fieldName, jsonObj);
      }
    } else {
      //对象类型
      String formatLog = formatLog(fieldVal);
      JSONObject parseObject = JSONObject.parseObject(formatLog);
      objCopy.put(field.getName(), parseObject);
    }
  }

  private static List<Field> getFieldListByClass(Class<? extends Object> clazz) {
    List<Field> fieldList = new ArrayList<>();
    while (clazz != null) {
      fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fieldList;
  }

  private static boolean isBasicType(Object obj) {
    return (obj instanceof String || obj instanceof Boolean || obj instanceof Number || obj instanceof Character);
  }

}
