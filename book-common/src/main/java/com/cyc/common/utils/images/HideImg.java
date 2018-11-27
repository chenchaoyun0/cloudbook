package com.cyc.common.utils.images;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 隐藏base64，或者过长字符串属性
 * @author chenchaoyun
 * @date 2017年8月8日 下午3:47:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface HideImg {

}
