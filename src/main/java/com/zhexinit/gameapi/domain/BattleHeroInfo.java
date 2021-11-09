package com.zhexinit.gameapi.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * 战斗中的英雄信息
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Data
public class BattleHeroInfo implements Serializable, Comparable<BattleHeroInfo> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4969724768063345938L;
	
	/**英雄id*/
    private Integer id;
	
	/**用户id*/
    private Integer userId;

    /**英雄名称*/
    private String name;

    /**生命值*/
    private Integer hp;

    /**攻击值*/
    private Integer atk;

    /**防御*/
    private Integer def;

    /**速度*/
    private Integer spd;
    
    /**九宫格上的位置信息，值为数字1-9*/
    private Integer position;
    
    /**每一次被攻击时所受的伤害*/
    private Integer hp_harm;
    
    /**剩余生命值*/
    private Integer hp_left;
    
    /**英雄初始怒气值*/
    private Integer fury;
    
    /**英雄当前剩余的怒气值*/
    private Integer fury_left;
    
    /**英雄是否还存活 true：存活  false：挂了*/
    private boolean isAlive = true;

	@Override
	public int compareTo(BattleHeroInfo o) {
		if (null == o) {
			return 1;
		} else if (this.position > o.getPosition()) {
			return 1;
		} else if (this.position == o.getPosition()) {
			return 0;
		} else {
			return -1;
		}
	}
   

}
