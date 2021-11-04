package com.zhexinit.gameapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhexinit.gameapi.entity.BattleRoundRecord;

/**
 * <p>
 * 游戏战斗回合信息记录表 Mapper 接口
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
public interface BattleRoundRecordMapper extends BaseMapper<BattleRoundRecord> {
	
	/**
	 * 插入战斗的回合记录，并获取插入记录的id
	 * @param battleRoundRecord
	 * @return  插入影响的记录条数
	 */
	public int insertReturnId(BattleRoundRecord battleRoundRecord);
}
