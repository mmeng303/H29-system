package com.example.tuanfei.service;

import com.example.tuanfei.Param.ActivityParam;
import com.example.tuanfei.entity.Activity;
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
public interface ActivityService extends IService<Activity> {

    List<Activity> selectByTeamId(ActivityParam activityParam);

    long selectByTeamCounts(ActivityParam activityParam);

    void updateByActivityId(Activity activity);
}
