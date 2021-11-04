package com.zhexinit.gameapi.service;

import com.zhexinit.gameapi.client.respinfo.BattleResultRespInfo;
import com.zhexinit.gameapi.exception.ParameterCheckException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
public interface GameBattleService {
	/**
	 * 获取一场战斗的战报
	 * 1、匹配战斗，从游戏用户中按照某种条件匹配出对战的用户
	 * 2、获取到对战双方用户战斗时所用的英雄列表
	 * 3、将英雄列表放到战斗位置（9宫格上的随机位置），然后进行战斗
	 * @return
	 */
	public BattleResultRespInfo doBattle() throws ParameterCheckException;
}
