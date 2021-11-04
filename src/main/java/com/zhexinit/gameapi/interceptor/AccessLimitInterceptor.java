package com.zhexinit.gameapi.interceptor;

import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.zhexinit.gameapi.annotation.AccessLimit;

import lombok.extern.slf4j.Slf4j;

/**
 * 接口防刷控制拦截器
 * @author ThinkPad
 *
 */
@Component
@Slf4j
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
	//引入redis
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("开始执行AccessLimitInterceptor::afterCompletion方法...");
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("开始执行AccessLimitInterceptor::afterConcurrentHandlingStarted方法...");
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("开始执行AccessLimitInterceptor::postHandle方法...");
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("开始执行AccessLimitInterceptor::preHandle方法...");
		
		//判断请求是否属于方法的请求
		if (handler instanceof HandlerMethod) {
			String requestUri = request.getRequestURI();
			String remoteIp = getRemortIP(request);
			
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			//无AccessLimit注解，标识接口请求不限制
			if (null == accessLimit) {
				log.info("执行AccessLimitInterceptor::preHandle方法，拦截请求地址:{}，客户端ip为：{}接口无访问限制...", requestUri, remoteIp);
				return true;
			}
			
			//获取注解配置的参数
			//单位时间
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			
			log.debug("执行AccessLimitInterceptor::preHandle方法，拦截请求地址:{}，客户端ip为：{}，单位时间{}秒内允许访问的最大次数为：{}，请求是否需要登录：{}...", 
					requestUri, remoteIp, seconds, maxCount, needLogin);
			/*
			 * 接口需要登录
			 * 1、请求头中传递某个值，代表用户的唯一标识，如用户名之类
			 * 2、根据请求头中的唯一标识去session或redis中获取用户信息
			 * 3、能获取到用户信息则流程继续往下走，未获取到用户信息则返回错误信息
			 */
			if(needLogin) {

//				String token = request.getHeader("token");
			}
			
			//从redis中获取当前接口在单位时间内访问的次数
			String key = ""; //redis的key，使用uri+用户标识组合起来作为键
			Object o = null; //根据key从redis中获取到的值
//			Object o = redisUtil.get(key);
			if(Objects.isNull(o)) {
//				// 第一次访问
//                redisUtil.incr(key, 1);
//                redisUtil.expire(key, seconds);
			} else {
//                // 获取单位时间内已访问次数
//                Integer count = Integer.valueOf(redisUtil.get(key).toString());
//                if(maxCount > count) {
//                    // 没超出访问限制
//                    redisUtil.incr(key, 1);
//                } else {
//                    // 超出访问限制
//                    logger.info("访问次数超出限制");
//                    Map<String, Object> failure = ResultUtil.getFailure(200, "访问次数超出限制");
//                    render(response, failure);
//                    return false;
//                }
            }
			
		}
		return true;
	}
	
	/**
	 * 获取客户端的请求ip
	 * @param request
	 * @return
	 */
	public String getRemortIP(HttpServletRequest request) {  
		if (request.getHeader("x-forwarded-for") == null) {  
			return request.getRemoteAddr();  
		}  
		return request.getHeader("x-forwarded-for");  
	  
	}  
	
    /**
     * 封装消息
     * @param response
     * @param message
     * @throws Exception
     */
    private void render(HttpServletResponse response, Map<String, Object> message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(message);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
	
}
