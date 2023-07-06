package com.example.tuanfei.service.impl;

import com.example.tuanfei.Param.FeeParam;
import com.example.tuanfei.entity.ActivityFee;
import com.example.tuanfei.mapper.ActivityFeeMapper;
import com.example.tuanfei.service.ActivityFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
@Service
public class ActivityFeeServiceImpl extends ServiceImpl<ActivityFeeMapper, ActivityFee> implements ActivityFeeService {

    @Resource
    private ActivityFeeMapper activityFeeMapper;

    @Override
    public List<ActivityFee> selectListPage(FeeParam feeParam) {
        return activityFeeMapper.selectListPage(feeParam);
    }

    @Override
    public long selectListCount(FeeParam feeParam) {
       return activityFeeMapper.selectFeeCount(feeParam);
    }
}
