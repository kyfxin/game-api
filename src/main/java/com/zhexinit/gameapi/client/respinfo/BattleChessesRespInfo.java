package com.zhexinit.gameapi.client.respinfo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 对战双方的英雄角色信息
 * @author ThinkPad
 *
 */
@Data
public class BattleChessesRespInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8714443266361367689L;
	
	/**挑战者（攻击方）的角色列表*/
	private List<BattleChessRespInfo> challenger;
	
	/**被挑战者（防守方）的角色列表*/
	private List<BattleChessRespInfo> defender;

}
