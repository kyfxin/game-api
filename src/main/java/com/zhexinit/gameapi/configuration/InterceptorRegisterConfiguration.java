package com.zhexinit.gameapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zhexinit.gameapi.interceptor.AccessLimitInterceptor;

/**
 * 扩展SpringMVC
 * SpringBoot2使用的Spring5，因此将WebMvcConfigurerAdapter改为WebMvcConfigurer
 * 使用WebMvcConfigurer扩展SpringMVC好处既保留了SpringBoot的自动配置，又能用到我们自己的配置
 */
//@EnableWebMvc //如果我们需要全面接管SpringBoot中的SpringMVC配置则开启此注解，
                //开启后，SpringMVC的自动配置将会失效。
/**
 * 拦截器注册
 * @author ThinkPad
 *
 */
@Configuration
public class InterceptorRegisterConfiguration implements WebMvcConfigurer {
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(getAccessLimitInterceptor()).addPathPatterns("/**");
	}

	@Bean
	public AccessLimitInterceptor getAccessLimitInterceptor() {
		return new AccessLimitInterceptor();
	}
	
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		//设置对“/”的请求映射到index
//		//如果没有数据返回到页面，没有必要用控制器方法对请求进行映射
//		registry.addViewController("/").setViewName("index");
//	}
}
