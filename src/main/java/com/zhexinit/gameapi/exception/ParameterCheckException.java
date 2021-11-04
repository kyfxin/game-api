package com.zhexinit.gameapi.exception;

/**
 * 参数校验异常
 * @author ThinkPad
 *
 */
public class ParameterCheckException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7087298902983146612L;
	
	public ParameterCheckException(String errorMsg) {
		super(errorMsg);
	}
}
