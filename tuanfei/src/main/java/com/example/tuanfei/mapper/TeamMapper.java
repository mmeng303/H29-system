package com.example.tuanfei.mapper;

import com.example.tuanfei.Param.InviteParam;
import com.example.tuanfei.entity.Team;
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
public interface TeamMapper extends BaseMapper<Team> {

    List<Team> selectOfNoJoin(InviteParam inviteParam);

    long selectOfNoJoinCount(InviteParam inviteParam);
}
