package com.example.tuanfei.entity;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Activity对象", description="")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    @TableField(value = "team_id")

    private Integer teamId;

    @TableField(value = "activity_name")
    private String activityName;
    @TableField(value = "starting_point")

    private String startingPoint;

    @TableField(value = "main_content")
    private String mainContent;

    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "team_fee")
    private BigDecimal teamFee;

    @ApiModelProperty(value = "是否完结,默认为0，手动点击完结变成1")
    @TableField(value = "is_completed")
    private Integer isCompleted=0;


}
