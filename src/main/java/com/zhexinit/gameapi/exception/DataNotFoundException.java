package com.zhexinit.gameapi.exception;

/**
 * 参数校验异常
 * @author ThinkPad
 *
 */
public class DataNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7087298902983146612L;
	
	public DataNotFoundException(String errorMsg) {
		super(errorMsg);
		
	}
}
