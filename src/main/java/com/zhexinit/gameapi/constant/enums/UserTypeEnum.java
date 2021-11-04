package com.zhexinit.gameapi.constant.enums;

/**
 * 用户类型集合
 * @author ThinkPad
 *
 */
public enum UserTypeEnum {
	ATTACK_USER(0, "攻击方"),
	DEFENCE_USER(1, "防御方");
	
	private int type;
	private String desc;
	
	private UserTypeEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public int value() {
		return this.type;
	}
	
	public String getDesc(int type) {
		for (UserTypeEnum elem : UserTypeEnum.values()) {
			if (elem.type == type) {
				return elem.desc;
			}
		}
		return "";
	}
}
