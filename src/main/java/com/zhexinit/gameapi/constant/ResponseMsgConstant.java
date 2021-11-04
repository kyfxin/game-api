package com.zhexinit.gameapi.constant;

/**
 * 客户端响应消息相关的常量
 * @author ThinkPad
 *
 */
public class ResponseMsgConstant {
	
	public static final String STATUS = "status";
	
	public static final String CODE = "code";
	
	public static final String DATA = "data";
	
	public static final String STATUS_SUCCESS = "success";
	
	public static final String STATUS_FALSE = "false";
	
	public static final String STATUS_ERROR = "error";
	
	public static final int CODE_200 = 200;
	
	public static final String MESSAGE = "message";
	
	public static final String MESSAGE_OK = "ok";
	
	public static final String GET_SYS_CONFIG_FAILED = "系统配置获取失败";
	
	public static final String CHECK_IDENTITY_FAILED = "姓名或身份证号码有误，请检查后再提交";
	
	public static final String UPDATE_PWD_ORIGIN_PWD_NULL = "原始密码为空";
	
	public static final String UPDATE_PWD_ORIGIN_PWD_ERROR = "原始密码错误";
	
	public static final String UPDATE_PWD_NEW_PWD_NULL = "新密码为空";
	
	public static final String UPDATE_PWD_NEW_PWD_NOT_EQUAL = "两次输入的新密码不一致";
}
