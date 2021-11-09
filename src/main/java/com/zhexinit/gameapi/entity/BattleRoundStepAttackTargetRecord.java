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
 * 游戏战斗回合攻击目标信息记录表
 * </p>
 *
 * @author wuqi
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BattleRoundStepAttackTargetRecord对象", description="游戏战斗回合攻击目标信息记录表")
public class BattleRoundStepAttackTargetRecord extends Model<BattleRoundStepAttackTargetRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序号，自增长")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "战斗记录id")
    private Integer battleRecordId;

    @ApiModelProperty(value = "游戏战斗回合id")
    private Integer battleRoundId;

    @ApiModelProperty(value = "游戏战斗回合攻击步骤id")
    private Integer battleRoundStepId;

    @ApiModelProperty(value = "防御的游戏用户id，即被攻击的用户id")
    private Integer defenceUserId;

    @ApiModelProperty(value = "防御的游戏用户所使用的英雄id，即被攻击的英雄id")
    private Integer defenceHeroId;

    @ApiModelProperty(value = "受伤害的生命值")
    private Integer hpHarm;

    @ApiModelProperty(value = "被攻击的英雄剩余生命值")
    private Integer hpLeft;

    @ApiModelProperty(value = "被攻击的位置 1-9")
    private Integer position;

    @ApiModelProperty(value = "防守英雄剩余的怒气")
    private Integer defenceHeroFuryLeft;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
