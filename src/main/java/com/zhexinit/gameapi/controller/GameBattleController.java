package com.zhexinit.gameapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhexinit.gameapi.client.respinfo.BattleResultRespInfo;
import com.zhexinit.gameapi.exception.ParameterCheckException;
import com.zhexinit.gameapi.service.GameBattleService;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@RestController
//@RequestMapping("/gameBattle")
public class GameBattleController {
	
	@Autowired
	private GameBattleService battleService;
	
	/**
	 * 获取一场战斗的战报
	 * 1、匹配战斗，从游戏用户中按照某种条件匹配出对战的用户
	 * 2、获取到对战双方用户战斗时所用的英雄列表
	 * 3、将英雄列表放到战斗位置（9宫格上的随机位置），然后进行战斗
	 * @return
	 * @throws ParameterCheckException 
	 */
	@GetMapping("/doBattle")
	public Object doBattle() throws ParameterCheckException {
		BattleResultRespInfo respInfo = battleService.doBattle();
		return respInfo;
	}
}
