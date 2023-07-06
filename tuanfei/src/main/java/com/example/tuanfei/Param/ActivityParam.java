package com.example.tuanfei.Param;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor(force = true)
@Getter
@Setter
public class ActivityParam {
    private Integer userId;
    private String  activityName;
    private Integer page;
    private Integer limit;
}
