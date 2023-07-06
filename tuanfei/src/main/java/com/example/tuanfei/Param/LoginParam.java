package com.example.tuanfei.Param;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(force = true)
public class LoginParam {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
