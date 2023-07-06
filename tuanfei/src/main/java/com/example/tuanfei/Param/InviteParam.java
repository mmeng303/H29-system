package com.example.tuanfei.Param;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class InviteParam {
    private Integer userId;
    private Integer teamId;
    private String memberName;
    private Integer page;
    private Integer limit;
    private Integer isJoin;
}
