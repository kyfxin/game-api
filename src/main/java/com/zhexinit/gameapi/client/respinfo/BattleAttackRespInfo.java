package com.zhexinit.gameapi.client.respinfo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 战报中发起攻击的对象信息
 * @author ThinkPad
 *
 */
@Data
public class BattleAttackRespInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3064894714664134470L;
	
	/**持方  0.挑战方1.被挑战方*/
	private Integer holder;
	
	/**发起攻击的位置1-9*/
	private Integer role;
	
	/**技能id 0：普通攻击*/
	private Integer skillId;
	
	/**被攻击的对象列表*/
	List<BattleAttackTargetRespInfo> targets;

}
