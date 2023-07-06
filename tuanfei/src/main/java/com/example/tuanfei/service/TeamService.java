package com.example.tuanfei.service;

import com.example.tuanfei.Param.InviteParam;
import com.example.tuanfei.entity.Team;
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
public interface TeamService extends IService<Team> {

    List<Team> selectOfNoJoin(InviteParam inviteParam);

    long selectOFnOJoinCount(InviteParam inviteParam);
}
