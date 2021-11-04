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
 * 战斗中用户所使用的英雄信息记录表
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BattleHeroRecord对象", description="战斗中用户所使用的英雄信息记录表")
public class BattleHeroRecord extends Model<BattleHeroRecord> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5378562059192307154L;

	@ApiModelProperty(value = "序号，自增长")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "战斗记录id")
    private Integer battleRecordId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "游戏用户类型  1：攻击方 2：防御方")
    private Integer userType;

    @ApiModelProperty(value = "英雄id")
    private Integer heroId;

    @ApiModelProperty(value = "生命值")
    private Integer hp;

    @ApiModelProperty(value = "攻击值")
    private Integer atk;

    @ApiModelProperty(value = "防御")
    private Integer def;

    @ApiModelProperty(value = "速度")
    private Integer spd;

    @ApiModelProperty(value = "位置1-9")
    private Integer position;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
