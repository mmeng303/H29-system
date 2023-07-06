package com.example.tuanfei.mapper;

import com.example.tuanfei.Param.FeeParam;
import com.example.tuanfei.entity.ActivityFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
@Repository
public interface ActivityFeeMapper extends BaseMapper<ActivityFee> {

    List<ActivityFee> selectListPage(FeeParam feeParam);

    long selectFeeCount(FeeParam feeParam);
}
