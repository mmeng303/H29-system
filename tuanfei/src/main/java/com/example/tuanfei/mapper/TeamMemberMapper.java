package com.example.tuanfei.mapper;

import com.example.tuanfei.Param.InviteParam;
import com.example.tuanfei.entity.TeamMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author java代写，就找小杨, QQ:1719036792
 * @since 2023-06-29
 */
@Repository
public interface TeamMemberMapper extends BaseMapper<TeamMember> {

    @Update("update team_member set is_join=1 where team_id=#{teamId} and user_id=#{userId}")
    void updateByJoin(InviteParam inviteParam);
}
