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
 * 英雄基础信息表
 * </p>
 *
 * @author wuqi
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HeroInfo对象", description="英雄基础信息表")
public class HeroInfo extends Model<HeroInfo> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4133182617600365300L;

	@ApiModelProperty(value = "序号，自增长")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "英雄名称")
    private String name;

    @ApiModelProperty(value = "生命值")
    private Integer hp;

    @ApiModelProperty(value = "攻击值")
    private Integer atk;

    @ApiModelProperty(value = "防御")
    private Integer def;

    @ApiModelProperty(value = "速度")
    private Integer spd;

    @ApiModelProperty(value = "英雄初始怒气值")
    private Integer fury;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
