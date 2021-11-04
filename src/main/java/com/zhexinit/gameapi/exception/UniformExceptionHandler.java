package com.zhexinit.gameapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局处理controller中的异常
 * @author ThinkPad
 *
 */
@RestControllerAdvice
public class UniformExceptionHandler {
	
//	/**
//	 * 处理
//	 * @return
//	 */
//	@ExceptionHandler(value = ParameterCheckException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public ResponseMsgWithoutData parameterCheckError(ParameterCheckException e) {
//		ResponseMsgWithoutData errorMsg = ResponseMsgUtil.buildFailedMsg(e.getMessage());
//		return errorMsg;
//	}
//	
//	/**
//	 * 处理
//	 * @return
//	 */
//	@ExceptionHandler(value = DataNotFoundException.class)
//	@ResponseStatus(HttpStatus.NOT_FOUND)
//	public ResponseMsgWithoutData dataNotFound(DataNotFoundException e) {
//		ResponseMsgWithoutData errorMsg = ResponseMsgUtil.buildNotFoundRespMsg(e.getMessage());
//		return errorMsg;
//	}
	
}
