package com.zhexinit.gameapi.constant.enums;

/**
 * 攻击类型
 * @author ThinkPad
 *
 */
public enum AttackSkillEnum {
	COMMON(0, "普通攻击"),
	THREE_HUNDRED_HURT(1, "对目标造成300%攻击伤害");
	
	private int type;
	private String desc;
	
	private AttackSkillEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public int value() {
		return this.type;
	}
	
	public String getDesc(int type) {
		for (AttackSkillEnum elem : AttackSkillEnum.values()) {
			if (elem.type == type) {
				return elem.desc;
			}
		}
		return "";
	}
}
