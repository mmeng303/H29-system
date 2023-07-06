package com.example.tuanfei.service;

import com.example.tuanfei.Param.InviteParam;
import com.example.tuanfei.entity.TeamMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
public interface TeamMemberService extends IService<TeamMember> {

    void updateByJoin(InviteParam inviteParam);
}
