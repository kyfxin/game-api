package com.zhexinit.gameapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhexinit.gameapi.entity.BattleRecord;

/**
 * <p>
 * 游戏战斗信息记录表 Mapper 接口
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
public interface BattleRecordMapper extends BaseMapper<BattleRecord> {
	/**
	 * 插入记录并获取插入的id
	 * @param battleRecord
	 * @return 插入记录影响的条数
	 */
	public int insertReturnId(BattleRecord battleRecord);
}
