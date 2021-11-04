package com.zhexinit.gameapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhexinit.gameapi.domain.BattleHeroInfo;
import com.zhexinit.gameapi.entity.UserHero;

/**
 * <p>
 * 游戏用户与英雄的关联信息表 Mapper 接口
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
public interface UserHeroMapper extends BaseMapper<UserHero> {
	/**
	 * 查询对战双方使用的英雄列表
	 * @param attackUserId   攻击方的用户id
	 * @param defenceUserId  防御方的用户id
	 * @return
	 */
	@Select("select " + 
			"		user_hero.user_id, " + 
			"		hero_info.* " + 
			"from " + 
			"		user_hero, hero_info " + 
			"where " + 
			"	user_hero.hero_id = hero_info.id " + 
			"and " + 
			"	(user_hero.user_id =#{attackUserId} or user_hero.user_id=#{defenceUserId})")
	public List<BattleHeroInfo> queryBattleHeroList(@Param("attackUserId") Integer attackUserId, 
			@Param("defenceUserId") Integer defenceUserId);
}
