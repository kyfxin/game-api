package com.zhexinit.gameapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口防刷
 * @author ThinkPad
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
	
	/**
	 * 请求的单位时间
	 * @return
	 */
	int seconds();
	
	/**
	 * 单位时间内允许的最大访问次数
	 * @return
	 */
	int maxCount();
	
	/**
	 * 接口请求是否需要登录
	 * @return
	 */
	boolean needLogin() default true;
}
