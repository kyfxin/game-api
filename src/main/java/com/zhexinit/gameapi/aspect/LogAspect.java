package com.zhexinit.gameapi.aspect;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zhexinit.gameapi.annotation.ControllerLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志记录切面
 * @author ThinkPad
 *
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
	
	/**
	 * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
	 */
	@Pointcut("@annotation(com.zhexinit.gameapi.annotation.ControllerLog)")
	public void controllerLogPointCut() {
		
	}
	
	/**
	 * 设置操作异常切入点记录异常日志 扫描所有controller包下操作，捕获controller的操作异常
	 */
	@Pointcut("execution(* com.zhexinit.gameapi.controller..*.*(..))")
	public void operExceptionLogPointCut() {
		
	}
	
	@Before(value="controllerLogPointCut()")
	public void doBefore(JoinPoint joinPoint) {
		log.info("LogAspect::doBefore........");
		//获取目标方法的参数信息
		Object[] obj = joinPoint.getArgs();
		for (int i = 0 ; i < obj.length; i ++) {
			log.info("LogAspect::doBefore obj[" + i + "] value is:" + obj[i]);
		}
		String kind = joinPoint.getKind();
		log.info("LogAspect::doBefore kink={}", kind);
//		SourceLocation sourceLocation = joinPoint.getSourceLocation();
//		if (null != sourceLocation) {
//			int column = sourceLocation.getColumn();
//			String fileName = sourceLocation.getFileName();
//			int line = sourceLocation.getLine();
//			String withinTypeName = sourceLocation.getWithinType().getName();
//			log.info("LogAspect::doBefore sourceLocation column={}, fileName={}, line={}, withinTypeName={}", 
//					column, fileName, line, withinTypeName);
//		} else {
//			log.info("LogAspect::doBefore sourceLocation is null...");
//		}
		
		
		StaticPart staticPart = joinPoint.getStaticPart();
		int id = staticPart.getId();
		String kind1 =  staticPart.getKind();
		log.info("LogAspect::doBefore startPart id={}, kind={}", id, kind1);
		 //AOP代理类的信息
		Object thisC = joinPoint.getThis();
		log.info("LogAspect::doBefore thisC is:{}", thisC.getClass().getName());
		//代理的目标对象
		Object target = joinPoint.getTarget();
		log.info("LogAspect::doBefore target i:{}", target.getClass());
		
		//用的最多 通知的签名 
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		log.info("LogAspect::doBefore 代理的是哪一个方法:{}", signature.getName());  
		//AOP代理类的名字  
		 //AOP代理类的名字  
		String declareName = signature.getDeclaringTypeName();
		log.info("AOP代理类的名字:{}", declareName);  
		
		 //获取RequestAttributes  
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();  
	    //从获取RequestAttributes中获取HttpServletRequest的信息  
	    HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);  
	    //如果要获取Session信息的话，可以这样写：  
	    //HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);  
	    //获取请求参数
	    
	  //获取请求参数
	    Enumeration<String> enumeration = request.getParameterNames();  
	    Map<String,String> parameterMap = Maps.newHashMap();  
	    while (enumeration.hasMoreElements()){  
	        String parameter = enumeration.nextElement();  
	        parameterMap.put(parameter,request.getParameter(parameter));  
	    }  
	    String str = JSON.toJSONString(parameterMap);  
	    if(obj.length > 0) {  
	        log.info("请求的参数信息为："+str);
	    }  
		
	
	}
	
	@After(value="controllerLogPointCut()")
	public void doAfter(JoinPoint joinPoint) {
		log.info("LogAspect::doAfter.........");
	}
	
	
	/**
	 * 后置返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
	 * 后置返回通知, 这里需要注意的是: 
	 *		如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息 
	 *		如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数 
	 *		returning：限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，
	 *		对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
	 * 
	 * @param joinPoint 切入点
	 * @param keys      返回结果
	 */
	@AfterReturning(value="controllerLogPointCut()", returning="keys")
	public void doAfterReturning(JoinPoint joinPoint, Object keys) {
		RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) requestAttr.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		
		// 从切面织入点处通过反射机制获取织入点处的方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		ControllerLog operLog = method.getAnnotation(ControllerLog.class);
		if (null != operLog) {
			String description = operLog.description();
			log.info("LogAspect::saveOperLog description={}", description);
		}
		
		// 获取请求的类名
		String className = joinPoint.getTarget().getClass().getName();
		// 获取请求的方法名
		String methodName = method.getName();
		log.info("LogAspect::saveOperLog className={}, methodName={}", className, methodName);
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		log.info("LogAspect::saveOperLog parameterMap={}, keys={}", parameterMap, keys);
		
	}
	
	/**
	 * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
	 * 
	 * @param joinPoint 切入点
	 * @param e         异常信息
	 */
	@AfterThrowing(pointcut="operExceptionLogPointCut()", throwing = "e")
	public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		log.info("LogAspect::saveExceptionLog Exception class is:{}", e.getClass());
		log.info("LogAspect::saveExceptionLog Exception message is:{}", e.getMessage());
	}
	
	@Around(value="controllerLogPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) {
		try {
			long startTime = System.currentTimeMillis();
			Object result = joinPoint.proceed();
			log.info("logAspect::around costTime={}", System.currentTimeMillis() - startTime);
			return result;
		} catch (Throwable e) {
			log.error("LogAspect::around exception:", e);
			return null;
		}
				
	}
}
