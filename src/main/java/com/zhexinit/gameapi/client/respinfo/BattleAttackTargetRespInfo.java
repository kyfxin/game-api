package com.zhexinit.gameapi.client.respinfo;

import java.io.Serializable;

import lombok.Data;

/**
 * 战报中被攻击的对象
 * @author ThinkPad
 *
 */
@Data
public class BattleAttackTargetRespInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4585853414837385261L;

	/**位置*/
	private Integer role;
	
	/**被攻击时受到的伤害值*/
	private Integer dmg;
}
