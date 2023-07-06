package com.example.tuanfei.Param;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class UserParam {
    private Integer limit;
    private Integer page;
    private String name;
}
