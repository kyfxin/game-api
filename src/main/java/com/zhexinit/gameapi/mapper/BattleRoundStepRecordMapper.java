package com.zhexinit.gameapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhexinit.gameapi.entity.BattleRoundStepRecord;

/**
 * <p>
 * 游戏战斗回合攻击步骤信息记录表 Mapper 接口
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
public interface BattleRoundStepRecordMapper extends BaseMapper<BattleRoundStepRecord> {
	/**
	 * 插入每个战斗回合中，每一步战斗记录，并获取记录的id
	 * @param record
	 * @return 插入影响的记录条数
	 */
	public int insertReturnId(BattleRoundStepRecord record);
}
