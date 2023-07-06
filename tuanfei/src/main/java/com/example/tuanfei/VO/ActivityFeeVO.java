package com.example.tuanfei.VO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
public class ActivityFeeVO {


    private String mainContent;

    private String activityName;

    private BigDecimal realTimeAaFee;

    private BigDecimal activityFee;

    private BigDecimal realTimeCostFee;
}
