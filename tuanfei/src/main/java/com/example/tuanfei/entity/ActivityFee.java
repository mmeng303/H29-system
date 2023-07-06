package com.example.tuanfei.entity;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value="ActivityFee对象", description="")
public class ActivityFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    private Integer activityId;

    private Integer memberId;

    private BigDecimal activityFee;

    private BigDecimal aaFee;

    private BigDecimal additionalFee;

    @TableField(exist = false)
    private BigDecimal realTimeAaFee;//实时计算的AA费用

    @TableField(exist = false)
    private BigDecimal realTimeCostFee;//实时计算的总费用

    @TableField(exist = false)
    private String activityName;  //活动名称


}
