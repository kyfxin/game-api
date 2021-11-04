package com.zhexinit.gameapi.client.respinfo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 战报结果响应消息
 * @author ThinkPad
 *
 */
@Data
public class BattleResultRespInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6948890492806100141L;

	/**游戏初始的角色信息*/
	private BattleChessesRespInfo info;
	
	/**战斗中的每一个攻击步骤*/
	private List<BattleAttackRespInfo> details;
}
