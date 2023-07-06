package com.example.tuanfei.service;

import com.example.tuanfei.Param.FeeParam;
import com.example.tuanfei.entity.ActivityFee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
public interface ActivityFeeService extends IService<ActivityFee> {

    List<ActivityFee> selectListPage(FeeParam feeParam);

    long selectListCount(FeeParam feeParam);
}
