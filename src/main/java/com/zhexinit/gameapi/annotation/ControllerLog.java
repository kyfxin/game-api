package com.zhexinit.gameapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller的日志记录
 * @author ThinkPad
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllerLog {
	/**
     * 方法描述
     */
    String description() default "";

    /**
     * 操作对象
     */
    String operands() default  "";

    /**
     * 操作类型
     */
    String operationType() default "";

    String extra() default "";

}
