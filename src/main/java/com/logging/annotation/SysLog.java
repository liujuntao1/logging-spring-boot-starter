package com.logging.annotation;

import java.lang.annotation.*;

/**
 * @author ljt
 * @date 2026/1/21
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * 模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    String operation() default "";

    /**
     * 是否持久化到数据库
     */
    boolean save() default true;
}
