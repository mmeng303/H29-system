package com.example.tuanfei.service.impl;

import com.example.tuanfei.Param.ActivityParam;
import com.example.tuanfei.entity.Activity;
import com.example.tuanfei.mapper.ActivityMapper;
import com.example.tuanfei.service.ActivityService;
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
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {


    @Resource
    private ActivityMapper activityMapper;
    @Override
    public List<Activity> selectByTeamId(ActivityParam activityParam) {
     return   activityMapper.selectByTeamId(activityParam);
    }

    @Override
    public long selectByTeamCounts(ActivityParam activityParam) {
        return activityMapper.selectByActivityCount(activityParam);
    }

    @Override
    public void updateByActivityId(Activity activity) {
        activityMapper.updateActivityId(activity);
    }
}
