package com.example.tuanfei.service.impl;

import com.example.tuanfei.Param.InviteParam;
import com.example.tuanfei.entity.Team;
import com.example.tuanfei.mapper.TeamMapper;
import com.example.tuanfei.service.TeamService;
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
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {


    @Resource
    private TeamMapper teamMapper;
    @Override
    public List<Team> selectOfNoJoin(InviteParam inviteParam) {
     return   teamMapper.selectOfNoJoin(inviteParam);
    }

    @Override
    public long selectOFnOJoinCount(InviteParam inviteParam) {
        return teamMapper.selectOfNoJoinCount(inviteParam);
    }
}
