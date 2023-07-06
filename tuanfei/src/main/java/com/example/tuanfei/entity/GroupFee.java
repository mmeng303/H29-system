package com.example.tuanfei.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel(value="GroupFee对象", description="")
public class GroupFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "独立缴纳团费")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer teamMemberId;

    private BigDecimal tuanfei;


}
