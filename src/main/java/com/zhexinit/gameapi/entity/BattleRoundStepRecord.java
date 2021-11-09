package com.zhexinit.gameapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 游戏战斗回合攻击步骤信息记录表
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BattleRoundStepRecord对象", description="游戏战斗回合攻击步骤信息记录表")
public class BattleRoundStepRecord extends Model<BattleRoundStepRecord> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1713447759269590901L;

	@ApiModelProperty(value = "序号，自增长")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "战斗记录id")
    private Integer battleRecordId;

    @ApiModelProperty(value = "游戏战斗回合id")
    private Integer battleRoundId;

    @ApiModelProperty(value = "回合中的第几步，从1开始")
    private Integer step;

    @ApiModelProperty(value = "主动攻击的游戏用户id")
    private Integer attackUserId;

    @ApiModelProperty(value = "主动攻击的游戏用户所使用的英雄id")
    private Integer attackHeroId;

    @ApiModelProperty(value = "发起攻击的英雄位置 1-9")
    private Integer attackPosition;

    @ApiModelProperty(value = "攻击技能 0：普通攻击")
    private Integer attackSkill;

    @ApiModelProperty(value = "攻击英雄剩余的怒气")
    private Integer attackHeroFuryLeft;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
