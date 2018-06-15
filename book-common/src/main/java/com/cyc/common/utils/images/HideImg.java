package com.cyc.common.utils.images;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不打印照片
 * @Description 在实体类中使用
 * @author chenchaoyun
 * @date 2017年8月8日 下午3:47:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface HideImg {

}
