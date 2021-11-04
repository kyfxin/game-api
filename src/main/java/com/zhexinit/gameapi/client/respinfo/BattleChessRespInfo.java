package com.zhexinit.gameapi.client.respinfo;

import java.io.Serializable;

import lombok.Data;

/**
 * 对战中的英雄角色信息
 * @author ThinkPad
 *
 */
@Data
public class BattleChessRespInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1262503303309392757L;
	
	/**英雄id*/
	private Integer cfgId;
	
	/**最大的血量*/
	private Integer hp;
	
	/**英雄的剩余血量*/
	private Integer hp_left;
	
	/**英雄攻击力*/
	private Integer atk;
	
	/**英雄的防御力*/
	private Integer def;
	
	/**战场位置 1-9*/
	private Integer pos;
	
	/**怒气值，默认50*/
	private Integer fury = 50;

}
