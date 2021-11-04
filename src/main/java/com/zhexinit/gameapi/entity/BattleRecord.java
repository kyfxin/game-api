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
 * 游戏战斗信息记录表
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BattleRecord对象", description="游戏战斗信息记录表")
public class BattleRecord extends Model<BattleRecord> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7977155569023942692L;

	@ApiModelProperty(value = "序号，自增长")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "战斗名称")
    private String name;

    @ApiModelProperty(value = "攻击方游戏用户id")
    private Integer attackUserId;

    @ApiModelProperty(value = "防御方游戏用户id")
    private Integer defenceUserId;

    @ApiModelProperty(value = "胜负方 1：攻击方， 2：防御方")
    private Integer victory;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
