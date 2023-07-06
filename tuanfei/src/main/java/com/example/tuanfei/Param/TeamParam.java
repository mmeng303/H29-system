
package com.example.tuanfei.Param;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class TeamParam {
private Integer teamId;
private String teamName;
private int page;
private int limit;
}
