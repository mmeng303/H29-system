package com.example.tuanfei.service.impl;

import com.example.tuanfei.Param.InviteParam;
import com.example.tuanfei.entity.TeamMember;
import com.example.tuanfei.mapper.TeamMemberMapper;
import com.example.tuanfei.service.TeamMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
@Service
public class TeamMemberServiceImpl extends ServiceImpl<TeamMemberMapper, TeamMember> implements TeamMemberService {

    @Resource
    private TeamMemberMapper teamMemberMapper;
    @Override
    public void updateByJoin(InviteParam inviteParam) {
        teamMemberMapper.updateByJoin(inviteParam);
    }
}
