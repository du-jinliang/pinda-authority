package cn.wenhe9.pinda.core.log.annotation;

import java.lang.annotation.*;

/**
 * @description: 操作日志注解
 * @author: DuJinliang
 * @create: 2023/7/22
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * 描述
     */
    String value();

    /**
     * 记录执行参数
     */
    boolean recordRequestParam() default true;

    /**
     * 记录返回参数
     */
    boolean recordResponseParam() default true;
}
